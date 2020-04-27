package com.devkproject.movieinfo.detail

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import io.reactivex.disposables.CompositeDisposable

class DetailRepository (private val apiService: TMDBInterface) {

    lateinit var detailDataSource: DetailDataSource

    fun getDetailMovie(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<TMDBDetail> {
        detailDataSource = DetailDataSource(apiService, compositeDisposable)
        detailDataSource.getSelectedMovieDetails(movieId)

        return detailDataSource.selectedMovieResponse
    }

    fun getDetailNetworkState(): LiveData<NetworkState> {
        return detailDataSource.networkState
    }
}