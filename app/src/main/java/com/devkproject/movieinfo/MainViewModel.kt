package com.devkproject.movieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.model.TMDBThumb
import com.devkproject.movieinfo.paging.TMDBPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class MainViewModel (private val tmdbPagedListRepository: TMDBPagedListRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val tmdbPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        tmdbPagedListRepository.getTMDBPagedList(compositeDisposable)
    }

    //메모리 효율을 위한 코드
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}