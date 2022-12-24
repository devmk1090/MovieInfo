package com.devkproject.movieinfo.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

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

    class UpcomingViewModelFactory(private val upcomingRepository: UpcomingRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
                UpcomingViewModel(upcomingRepository) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}