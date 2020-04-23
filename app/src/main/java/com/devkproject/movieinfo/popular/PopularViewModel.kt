package com.devkproject.movieinfo.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class PopularViewModel (private val popularRepository: PopularRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val popularPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        popularRepository.getPopularPagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        popularRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return popularPagedList.value?.isEmpty()?: true
    }

    //메모리 효율을 위한 코드
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}