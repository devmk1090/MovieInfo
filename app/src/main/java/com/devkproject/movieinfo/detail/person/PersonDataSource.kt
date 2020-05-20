package com.devkproject.movieinfo.detail.person

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBPerson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PersonDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _personResponse = MutableLiveData<TMDBPerson>()
    val personResponse: LiveData<TMDBPerson>
        get() = _personResponse

    fun getPerson(personId: Int) {
        try {
            compositeDisposable.add(
                apiService.getPerson(personId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _personResponse.postValue(it)
                    }, {
                        Log.e("PersonDataSource", it.message)
                    })
            )
        } catch (e: Exception) {
            Log.e("PersonDataSource", e.message)
        }
    }
}