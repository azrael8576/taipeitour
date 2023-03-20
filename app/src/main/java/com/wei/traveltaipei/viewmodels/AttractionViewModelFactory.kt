package com.wei.traveltaipei.viewmodels

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.wei.traveltaipei.repository.AttractionRepository

/**
 * Factory for creating a [HahowClassListViewModel] with a constructor that takes a [DataRepository].
 */
class AttractionViewModelFactory(
    private val repository: AttractionRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return AttractionViewModel(repository, handle) as T
    }
}
