package com.android.example.paging.pagingwithnetwork.reddit.repository.inDb

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alex.cathaybk_recruit_android.MainApplication
import com.alex.cathaybk_recruit_android.api.TravelTaipeiService
import com.alex.cathaybk_recruit_android.db.AttractionDao
import com.alex.cathaybk_recruit_android.db.AttractionRemoteKeyDao
import com.alex.cathaybk_recruit_android.db.TravelTaipeiDb
import com.alex.cathaybk_recruit_android.utilities.isNetSystemUsable
import com.alex.cathaybk_recruit_android.vo.Attraction
import com.alex.cathaybk_recruit_android.vo.AttractionRemoteKey
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: TravelTaipeiDb,
    private val travelTaipeiService: TravelTaipeiService,
    private val langName: String,
) : RemoteMediator<Int, Attraction>() {
    private val attractionDao: AttractionDao = db.attractionDao()
    private val remoteKeyDao: AttractionRemoteKeyDao = db.remoteKeys()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Attraction>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val pageKey = when (loadType) {
                REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPageKey?.minus(1) ?: 1
                }
                PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPageKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                APPEND -> {

                    // Query DB for LangRemoteKeyDao for the lang.
                    // LangRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the TravelTaipei API to fetch the next or previous page.
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to TravelTaipei API will fetch the initial page.
                    val pageKey = remoteKeys?.nextPageKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )

                    pageKey
                }
            }

            if (!isNetSystemUsable(MainApplication.appContext)) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response  = travelTaipeiService.getAttractions(
                lang = langName,
                categoryIds = null,
                nlat = null,
                elong = null,
                page = pageKey,
            ).data

            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (pageKey == 1) null else pageKey - 1
            val nextPage = if (endOfPaginationReached) null else pageKey + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    attractionDao.clear()
                    remoteKeyDao.clear()
                }
                val keys = response.map { it ->
                    AttractionRemoteKey(
                        id = it.id,
                        prevPageKey = prevPage,
                        nextPageKey = nextPage
                    )
                }
                attractionDao.insertAll(attractions = response)
                remoteKeyDao.insert(keys = keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Attraction>
    ): AttractionRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.remoteKeyByAttraction(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Attraction>
    ): AttractionRemoteKey? {
        return state.pages.firstOrNull{ it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { attraction ->
                remoteKeyDao.remoteKeyByAttraction(id = attraction.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Attraction>
    ): AttractionRemoteKey? {
        return state.pages.lastOrNull(){ it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { attraction ->
                remoteKeyDao.remoteKeyByAttraction(id = attraction.id)
            }
    }
}
