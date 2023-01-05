package com.alex.cathaybk_recruit_android.utilities

import androidx.fragment.app.Fragment
import com.alex.cathaybk_recruit_android.repository.AttractionRepository
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModelFactory


/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun provideAttractionViewModelFactory(attractionRepository: AttractionRepository, fragment: Fragment): AttractionViewModelFactory {
        return AttractionViewModelFactory(attractionRepository, fragment)
    }
}
