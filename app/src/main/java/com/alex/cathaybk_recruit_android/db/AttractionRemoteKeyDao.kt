package com.alex.cathaybk_recruit_android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alex.cathaybk_recruit_android.vo.AttractionRemoteKey

@Dao
interface AttractionRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<AttractionRemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeyByAttraction(id: Int): AttractionRemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun clear()
}
