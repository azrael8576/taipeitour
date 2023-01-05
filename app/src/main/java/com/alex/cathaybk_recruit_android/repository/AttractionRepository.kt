package com.alex.cathaybk_recruit_android.repository

import androidx.paging.PagingData
import com.alex.cathaybk_recruit_android.vo.Attraction
import kotlinx.coroutines.flow.Flow

/**
 * Common interface shared by the different repository implementations.
 * Note: this only exists for sample purposes - typically an app would implement a repo once, either
 * network+db, or network-only
 */
interface AttractionRepository {
    fun getPagingData(lang: String): Flow<PagingData<Attraction>>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}