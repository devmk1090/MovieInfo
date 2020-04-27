package com.devkproject.movieinfo.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class FavoriteDataSource(private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _favoriteResponse = MutableLiveData<TMDBThumb>()
    val favoriteResponse: LiveData<TMDBThumb>
        get() = _favoriteResponse

    fun getFavoriteMovie(movieId: Int) {
        try {
            compositeDisposable.add(
                apiService.getFavoriteMovie(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _favoriteResponse.postValue(it)
                    }, {
                        Log.e("FavoriteDataSource", it.message)
                    })
            )
        } catch (e: Exception) {
            Log.e("FavoriteDataSource", e.message)
        }
    }
}