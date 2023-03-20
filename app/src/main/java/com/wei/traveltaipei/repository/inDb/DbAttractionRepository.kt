package com.wei.traveltaipei.repository.inDb

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wei.traveltaipei.api.TravelTaipeiService
import com.wei.traveltaipei.db.TravelTaipeiDb
import com.wei.traveltaipei.repository.AttractionRepository
import com.wei.traveltaipei.vo.Attraction
import kotlinx.coroutines.flow.Flow

/**
 * Repository implementation that uses a database backed [androidx.paging.PagingSource] and
 * [androidx.paging.RemoteMediator] to load pages from network when there are no more items cached
 * in the database to load.
 */
class DbAttractionRepository(
    val db: TravelTaipeiDb,
    val travelTaipeiService: TravelTaipeiService
    ) : AttractionRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingData(lang: String): Flow<PagingData<Attraction>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = TIMES_PREFETCH * PAGE_SIZE,
                initialLoadSize = TIMES_INIT_LOAD * PAGE_SIZE,
            ),
            remoteMediator = PageKeyedRemoteMediator(db, travelTaipeiService, lang)
        ) {
            db.attractionDao().get()
        }.flow
    }

    companion object {
        const val PAGE_SIZE = 10
        const val TIMES_PREFETCH = 3
        const val TIMES_INIT_LOAD = 2
    }
}
