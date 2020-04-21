package com.devkproject.movieinfo.detail.credits

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBCredits
import io.reactivex.disposables.CompositeDisposable

class CreditsRepository (private val apiService: TMDBInterface) {
    private lateinit var creditsDataSource: CreditsDataSource

    fun getCreditsMovie(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<TMDBCredits> {
        creditsDataSource = CreditsDataSource(
            apiService,
            compositeDisposable
        )
        creditsDataSource.getCreditsMovie(movieId)
        return creditsDataSource.creditsMovieResponse
    }
}