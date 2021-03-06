package com.devkproject.movieinfo.toprated

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

class TopRatedViewModel (private val topRatedRepository: TopRatedRepository)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val topRatedPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        topRatedRepository.getTMDBTopRatedPagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        topRatedRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return topRatedPagedList.value?.isEmpty()?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class TopRatedViewModelFactory(private val topRatedRepository: TopRatedRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(TopRatedViewModel::class.java)) {
                TopRatedViewModel(topRatedRepository) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}