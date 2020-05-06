package com.devkproject.movieinfo.detail.videos

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBVideos
import io.reactivex.disposables.CompositeDisposable

class VideosRepository (private val apiService: TMDBInterface) {

    private lateinit var videosDataSource: VideosDataSource

    fun getVideosMovie(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<TMDBVideos> {
        videosDataSource = VideosDataSource(apiService, compositeDisposable)
        videosDataSource.getVideosMovie(movieId)
        return videosDataSource.videosMovieResponse
    }
}