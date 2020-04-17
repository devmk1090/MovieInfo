package com.devkproject.movieinfo.selected

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class SelectedDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _selectedMovieResponse = MutableLiveData<TMDBDetail>()
    val selectedMovieResponse: LiveData<TMDBDetail>
        get() = _selectedMovieResponse

    fun getSelectedMovieDetails(movieId: Int) {

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _selectedMovieResponse.postValue(it)
                        }, {
                            Log.e("SelectedDataSource", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("SelectedDataSource", e.message)
        }
    }
}