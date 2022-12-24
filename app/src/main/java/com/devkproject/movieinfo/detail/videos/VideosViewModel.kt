package com.devkproject.movieinfo.detail.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devkproject.movieinfo.model.TMDBVideos
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

class VideosViewModel (private val videosRepository: VideosRepository, movieId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val videosMovie: LiveData<TMDBVideos> by lazy {
        videosRepository.getVideosMovie(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class VideosViewModelFactory(private val videosRepository: VideosRepository, private val movieId: Int): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(VideosViewModel::class.java)) {
                VideosViewModel(videosRepository, movieId) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}