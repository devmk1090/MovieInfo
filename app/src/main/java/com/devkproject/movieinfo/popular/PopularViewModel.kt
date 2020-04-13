package com.devkproject.movieinfo.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class PopularViewModel (private val tmdbRepository: PopularRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tmdbPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        tmdbRepository.getTMDBPagedList(compositeDisposable)
    }

    //메모리 효율을 위한 코드
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}