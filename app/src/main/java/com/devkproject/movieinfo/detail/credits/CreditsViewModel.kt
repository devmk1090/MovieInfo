package com.devkproject.movieinfo.detail.credits

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devkproject.movieinfo.model.TMDBCredits
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

class CreditsViewModel(private val creditsRepository: CreditsRepository, movieId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val creditsMovie: LiveData<TMDBCredits> by lazy {
        creditsRepository.getCreditsMovie(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class CreditsViewModelFactory(private val creditsRepository: CreditsRepository, private val movieId: Int): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CreditsViewModel::class.java)) {
                CreditsViewModel(creditsRepository, movieId) as T
            } else {
                throw IllegalArgumentException()
            }
        }

    }
}