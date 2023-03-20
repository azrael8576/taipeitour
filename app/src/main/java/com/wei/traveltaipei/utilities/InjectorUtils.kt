package com.wei.traveltaipei.utilities

import androidx.fragment.app.Fragment
<<<<<<< HEAD:app/src/main/java/com/wei/traveltaipei/utilities/InjectorUtils.kt
import com.wei.traveltaipei.repository.AttractionRepository
import com.wei.traveltaipei.viewmodels.AttractionViewModelFactory
=======
import com.alex.cathaybk_recruit_android.repository.AttractionRepository
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModelFactory
>>>>>>> origin/develop:app/src/main/java/com/alex/cathaybk_recruit_android/utilities/InjectorUtils.kt

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun provideAttractionViewModelFactory(attractionRepository: AttractionRepository, fragment: Fragment): AttractionViewModelFactory {
        return AttractionViewModelFactory(attractionRepository, fragment)
    }
}
