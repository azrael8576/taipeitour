package com.alex.cathaybk_recruit_android.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alex.cathaybk_recruit_android.vo.Attraction

@Dao
interface AttractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attractions: List<Attraction>)

    @Query("SELECT * FROM attractions ORDER BY modified DESC")
    fun get(): PagingSource<Int, Attraction>

    @Query("DELETE FROM attractions")
    suspend fun clear()
}
