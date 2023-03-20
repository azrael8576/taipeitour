package com.wei.taipeitour.utilities

import androidx.fragment.app.Fragment
import com.wei.taipeitour.repository.AttractionRepository
import com.wei.taipeitour.viewmodels.AttractionViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun provideAttractionViewModelFactory(
        attractionRepository: AttractionRepository,
        fragment: Fragment
    ): AttractionViewModelFactory {
        return AttractionViewModelFactory(attractionRepository, fragment)
    }
}
