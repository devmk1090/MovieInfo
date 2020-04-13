package com.devkproject.movieinfo.selected

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import io.reactivex.disposables.CompositeDisposable

class SelectedRepository (private val apiService: TMDBInterface) {

    lateinit var tmdbSelectedDataSource: SelectedDataSource

    fun getSelectedDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<TMDBDetail> {
        tmdbSelectedDataSource =
            SelectedDataSource(apiService, compositeDisposable)
        tmdbSelectedDataSource.getSelectedMovieDetails(movieId)

        return tmdbSelectedDataSource.selectedMovieResponse
    }
}