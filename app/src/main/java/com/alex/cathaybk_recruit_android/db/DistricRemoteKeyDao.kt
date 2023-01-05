package com.alex.cathaybk_recruit_android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alex.cathaybk_recruit_android.vo.DistricRemoteKey

@Dao
interface DistricRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: DistricRemoteKeyDao)

    @Query("SELECT * FROM remote_keys WHERE distric = :distric")
    suspend fun remoteKeyByAttraction(distric: String): DistricRemoteKey

    @Query("DELETE FROM remote_keys WHERE distric = :distric")
    suspend fun deleteByDistric(distric: String)
}