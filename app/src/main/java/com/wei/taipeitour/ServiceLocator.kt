package com.wei.taipeitour

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wei.taipeitour.api.TravelTaipeiService
import com.wei.taipeitour.db.TaipeiTourDb
import com.wei.taipeitour.repository.AttractionRepository
import com.wei.taipeitour.repository.inDb.DbAttractionRepository

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application,
                        useInMemoryDb = false
                    )
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(type: AttractionRepository.Type): AttractionRepository

    fun getTravelTaipeiService(): TravelTaipeiService
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) : ServiceLocator {
    private val db by lazy {
        TaipeiTourDb.create(app, useInMemoryDb)
    }

    private val api by lazy {
        TravelTaipeiService.create()
    }

    override fun getRepository(type: AttractionRepository.Type): AttractionRepository {
        return when (type) {
            // TODO IN_MEMORY_BY_ITEM, IN_MEMORY_BY_PAGE Repository
            AttractionRepository.Type.IN_MEMORY_BY_ITEM -> DbAttractionRepository(
                db = db,
                travelTaipeiService = getTravelTaipeiService()
            )
            AttractionRepository.Type.IN_MEMORY_BY_PAGE -> DbAttractionRepository(
                db = db,
                travelTaipeiService = getTravelTaipeiService()
            )

            AttractionRepository.Type.DB -> DbAttractionRepository(
                db = db,
                travelTaipeiService = getTravelTaipeiService()
            )
        }
    }

    override fun getTravelTaipeiService(): TravelTaipeiService = api
}
