package com.devkproject.movieinfo.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class DetailDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _detailMovieResponse = MutableLiveData<TMDBDetail>()
    val selectedMovieResponse: LiveData<TMDBDetail>
        get() = _detailMovieResponse

    fun getSelectedMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _detailMovieResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)

                        }, {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("DetailDataSource", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("DetailDataSource", e.message)
        }
    }
}