package com.devkproject.movieinfo.detail.credits

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBCredits
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class CreditsDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) {

    //커스텀 접근자 (getter 커스텀)
    //val 로 선언하면 setter 는 없다
    private val _creditsMovieResponse = MutableLiveData<TMDBCredits>()
    val creditsMovieResponse: LiveData<TMDBCredits>
        get() = _creditsMovieResponse

    fun getCreditsMovie(movieId: Int) {
        try {
            compositeDisposable.add(
                apiService.getMovieCredits(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _creditsMovieResponse.postValue(it)
                    }, {
                        Log.e("CreditsDataSource", it.message!!)
                    })
            )
        } catch (e: Exception) {
            Log.e("CreditsDataSource", e.message.toString())
        }
    }
}