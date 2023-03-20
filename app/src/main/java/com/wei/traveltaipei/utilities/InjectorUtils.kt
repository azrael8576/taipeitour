package com.wei.traveltaipei.utilities

import androidx.fragment.app.Fragment
import com.wei.traveltaipei.repository.AttractionRepository
import com.wei.traveltaipei.viewmodels.AttractionViewModelFactory

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
