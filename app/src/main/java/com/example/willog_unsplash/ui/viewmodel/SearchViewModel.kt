package com.example.willog_unsplash.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.willog_unsplash.data.model.PhotoData
import com.example.willog_unsplash.data.repository.PhotoSearchRepo
import com.example.willog_unsplash.navigation.AppNavigator
import com.example.willog_unsplash.ui.events.BookmarkEvent
import com.example.willog_unsplash.ui.events.SearchEvent
import com.example.willog_unsplash.ui.states.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val photoSearchRepo: PhotoSearchRepo
) : BaseViewModel(appNavigator) {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    // Paging
    private val _searchPagingResult: MutableStateFlow<PagingData<PhotoData>> =
        MutableStateFlow<PagingData<PhotoData>>(PagingData.empty())

    val searchPagingResult: StateFlow<PagingData<PhotoData>> = _searchPagingResult.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.GetSearchQuery -> searchImage(event.query)
            is SearchEvent.ClickImage -> clickImage()
            is SearchEvent.ClickBookMark -> clickBookMark()
        }
    }

    private fun searchImage(query: String) {
        viewModelScope.launch {
            photoSearchRepo.searchPhotosPaging(query)
                .cachedIn(viewModelScope)
                .collect {
                    _searchPagingResult.value = it
                }
        }
    }

    private fun clickImage() {
        /* TODO */
    }

    private fun clickBookMark() {
        /* TODO */
    }

}