package com.devkproject.movieinfo.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.model.TMDBDetail
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

class DetailViewModel (private val detailRepository: DetailRepository, movieId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val detailMovie: LiveData<TMDBDetail> by lazy {
        detailRepository.getDetailMovie(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        detailRepository.getDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class DetailViewModelFactory(private val detailRepository: DetailRepository, private val movieId: Int): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                DetailViewModel(detailRepository, movieId) as T
            } else {
                throw IllegalArgumentException()
            }
        }

    }
}