package com.wei.traveltaipei.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wei.traveltaipei.utilities.TRAVEL_TAIPEI_DB_NAME
import com.wei.traveltaipei.utilities.TRAVEL_TAIPEI_DB_VERSION
import com.wei.traveltaipei.vo.Attraction
import com.wei.traveltaipei.vo.AttractionRemoteKey

/**
 * Database schema used by the DbAttractionRepository
 */
@Database(
    entities = [Attraction::class, AttractionRemoteKey::class],
    version = TRAVEL_TAIPEI_DB_VERSION,
    exportSchema = false
)
abstract class TravelTaipeiDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): TravelTaipeiDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, TravelTaipeiDb::class.java)
            } else {
                Room.databaseBuilder(context, TravelTaipeiDb::class.java, TRAVEL_TAIPEI_DB_NAME)
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun attractionDao(): AttractionDao
    abstract fun remoteKeys(): AttractionRemoteKeyDao
}
