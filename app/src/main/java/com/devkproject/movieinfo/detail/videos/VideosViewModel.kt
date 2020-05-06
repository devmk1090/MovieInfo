package com.devkproject.movieinfo.detail.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.devkproject.movieinfo.model.TMDBVideos
import io.reactivex.disposables.CompositeDisposable

class VideosViewModel (private val videosRepository: VideosRepository, movieId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val videosMovie: LiveData<TMDBVideos> by lazy {
        videosRepository.getVideosMovie(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}