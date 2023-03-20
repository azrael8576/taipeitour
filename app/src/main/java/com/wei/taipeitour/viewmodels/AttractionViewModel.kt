package com.wei.taipeitour.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wei.taipeitour.repository.AttractionRepository
import com.wei.taipeitour.utilities.APP_DEFAULT_LANG
import com.wei.taipeitour.utilities.SHARED_PREF_KEY_LANG
import com.wei.taipeitour.vo.Attraction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

class AttractionViewModel(
    private val repository: AttractionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        if (!savedStateHandle.contains(SHARED_PREF_KEY_LANG)) {
            savedStateHandle.set(SHARED_PREF_KEY_LANG, APP_DEFAULT_LANG)
        }
    }

    private val _clickedAttraction = MutableLiveData<Attraction>()
    val clickedAttraction: LiveData<Attraction> get() = _clickedAttraction

    fun setClickedAttraction(attraction: Attraction) {
        _clickedAttraction.value = attraction
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val attractionList = savedStateHandle.getLiveData<String>(SHARED_PREF_KEY_LANG)
        .asFlow()
        .flatMapLatest { repository.getPagingData(it) }
        // cachedIn() shares the paging state across multiple consumers of posts,
        // e.g. different generations of UI across rotation config change
        .cachedIn(viewModelScope)

    fun showLang(lang: String) {
        if (!shouldShowLang(lang)) return
        savedStateHandle.set(SHARED_PREF_KEY_LANG, lang)
    }

    private fun shouldShowLang(lang: String): Boolean {
        return savedStateHandle.get<String>(SHARED_PREF_KEY_LANG) != lang
    }
}
