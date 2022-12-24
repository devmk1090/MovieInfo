package com.devkproject.movieinfo.detail.videos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBVideos
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class VideosDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _videosMovieResponse = MutableLiveData<TMDBVideos>()
    val videosMovieResponse: LiveData<TMDBVideos>
        get() = _videosMovieResponse

    fun getVideosMovie(movieId: Int) {
        try {
            compositeDisposable.add(
                apiService.getMovieVideos(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _videosMovieResponse.postValue(it)
                    }, {
                        Log.e("VideosDataSource", it.message!!)
                    })
            )
        } catch (e: Exception) {
            Log.e("VideosDataSource", e.message.toString())
        }
    }
}