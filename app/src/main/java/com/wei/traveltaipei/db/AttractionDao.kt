package com.wei.traveltaipei.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
<<<<<<< HEAD:app/src/main/java/com/wei/traveltaipei/db/AttractionDao.kt
import com.wei.traveltaipei.vo.Attraction
=======
import com.alex.cathaybk_recruit_android.vo.Attraction
>>>>>>> origin/develop:app/src/main/java/com/alex/cathaybk_recruit_android/db/AttractionDao.kt

@Dao
interface AttractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attractions: List<Attraction>)

    @Query("SELECT * FROM attractions ORDER BY modified DESC")
    fun get(): PagingSource<Int, Attraction>

    @Query("DELETE FROM attractions")
    suspend fun clear()
}
