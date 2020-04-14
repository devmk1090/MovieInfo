package com.devkproject.movieinfo.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class SearchViewModel (private val searchRepository: SearchRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val searchPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        searchRepository.getSearchPagedList(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}