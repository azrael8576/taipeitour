package com.alex.cathaybk_recruit_android.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alex.cathaybk_recruit_android.vo.Attraction

/**
 * Database schema used by the DbRedditPostRepository
 */
@Database(
    entities = [Attraction::class, DistricRemoteKeyDao::class],
    version = 1,
    exportSchema = false
)
abstract class TravelTaipeiDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): TravelTaipeiDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, TravelTaipeiDb::class.java)
            } else {
                Room.databaseBuilder(context, TravelTaipeiDb::class.java, "travel.taipei.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun attractions(): AttractionDao
    abstract fun remoteKeys(): DistricRemoteKeyDao
}