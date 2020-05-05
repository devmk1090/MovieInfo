package com.devkproject.movieinfo.detail.credits

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.devkproject.movieinfo.model.TMDBCredits
import io.reactivex.disposables.CompositeDisposable

class CreditsViewModel(private val creditsRepository: CreditsRepository, movieId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val creditsMovie: LiveData<TMDBCredits> by lazy {
        creditsRepository.getCreditsMovie(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}