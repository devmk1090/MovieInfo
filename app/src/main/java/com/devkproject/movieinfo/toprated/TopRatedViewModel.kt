package com.devkproject.movieinfo.toprated

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class TopRatedViewModel (private val tmdbTopRatedRepository: TopRatedRepository)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tmdbTopRatedPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        tmdbTopRatedRepository.getTMDBTopRatedPagedList(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}