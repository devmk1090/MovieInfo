package com.devkproject.movieinfo.detail

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import io.reactivex.disposables.CompositeDisposable

class DetailRepository (private val apiService: TMDBInterface) {

    lateinit var tmdbSelectedDataSource: DetailDataSource

    fun getSelectedDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<TMDBDetail> {
        tmdbSelectedDataSource =
            DetailDataSource(apiService, compositeDisposable)
        tmdbSelectedDataSource.getSelectedMovieDetails(movieId)

        return tmdbSelectedDataSource.selectedMovieResponse
    }
}