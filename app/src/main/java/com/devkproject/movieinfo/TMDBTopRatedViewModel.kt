package com.devkproject.movieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class TMDBTopRatedViewModel (private val tmdbTopRatedRepository: TMDBTopRatedRepository)
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