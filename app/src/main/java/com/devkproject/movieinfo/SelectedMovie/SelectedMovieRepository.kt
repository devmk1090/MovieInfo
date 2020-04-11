package com.devkproject.movieinfo.SelectedMovie

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import com.devkproject.movieinfo.paging.TMDBSelectedDataSource
import io.reactivex.disposables.CompositeDisposable

class SelectedMovieRepository (private val apiService: TMDBInterface) {

    lateinit var tmdbSelectedDataSource: TMDBSelectedDataSource

    fun getSelectedDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<TMDBDetail> {
        tmdbSelectedDataSource = TMDBSelectedDataSource(apiService, compositeDisposable)
        tmdbSelectedDataSource.getSelectedMovieDetails(movieId)

        return tmdbSelectedDataSource.selectedMovieResponse
    }
}