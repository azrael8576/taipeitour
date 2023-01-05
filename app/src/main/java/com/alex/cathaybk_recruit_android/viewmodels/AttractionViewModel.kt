package com.alex.cathaybk_recruit_android.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.alex.cathaybk_recruit_android.repository.AttractionRepository
import com.alex.cathaybk_recruit_android.utilities.APP_DEFAULT_LANG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

class AttractionViewModel(
    private val repository: AttractionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val KEY_LANG = "lang"
    }

    init {
        if (!savedStateHandle.contains(KEY_LANG)) {
            savedStateHandle.set(KEY_LANG, APP_DEFAULT_LANG)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val attractionList = savedStateHandle.getLiveData<String>(KEY_LANG)
        .asFlow()
        .flatMapLatest { repository.getPagingData(APP_DEFAULT_LANG) }
        // cachedIn() shares the paging state across multiple consumers of posts,
        // e.g. different generations of UI across rotation config change
        .cachedIn(viewModelScope)
}