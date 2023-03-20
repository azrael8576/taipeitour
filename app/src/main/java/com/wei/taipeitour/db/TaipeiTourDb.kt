package com.wei.taipeitour.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wei.taipeitour.utilities.TRAVEL_TAIPEI_DB_NAME
import com.wei.taipeitour.utilities.TRAVEL_TAIPEI_DB_VERSION
import com.wei.taipeitour.vo.Attraction
import com.wei.taipeitour.vo.AttractionRemoteKey

/**
 * Database schema used by the DbAttractionRepository
 */
@Database(
    entities = [Attraction::class, AttractionRemoteKey::class],
    version = TRAVEL_TAIPEI_DB_VERSION,
    exportSchema = false
)
abstract class TaipeiTourDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): TaipeiTourDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, TaipeiTourDb::class.java)
            } else {
                Room.databaseBuilder(context, TaipeiTourDb::class.java, TRAVEL_TAIPEI_DB_NAME)
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun attractionDao(): AttractionDao
    abstract fun remoteKeys(): AttractionRemoteKeyDao
}
