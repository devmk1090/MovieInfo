package com.devkproject.movieinfo.selected

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.devkproject.movieinfo.model.TMDBDetail
import io.reactivex.disposables.CompositeDisposable

class SelectedViewModel (private val selectedMovieRepository: SelectedRepository, movieId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val selectedMovie: LiveData<TMDBDetail> by lazy {
        selectedMovieRepository.getSelectedDetails(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}