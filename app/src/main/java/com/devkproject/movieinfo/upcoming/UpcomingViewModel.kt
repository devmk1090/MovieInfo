package com.devkproject.movieinfo.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class UpcomingViewModel (private val upcomingRepository: UpcomingRepository)
    :ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val upcomingPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        upcomingRepository.getUpcomingPagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        upcomingRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return upcomingPagedList.value?.isEmpty()?: true
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}