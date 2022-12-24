package com.devkproject.movieinfo.detail.person

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBPerson
import com.devkproject.movieinfo.model.TMDBPersonDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PersonDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _personResponse = MutableLiveData<TMDBPerson>()
    val personResponse: LiveData<TMDBPerson>
        get() = _personResponse

    private val _personDetailResponse = MutableLiveData<TMDBPersonDetail>()
    val personDetailResponse: LiveData<TMDBPersonDetail>
        get() = _personDetailResponse

    fun getPerson(personId: Int) {
        try {
            compositeDisposable.add(
                apiService.getPerson(personId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _personResponse.postValue(it)
                    }, {
                        Log.e("PersonDataSource", it.message!!)
                    })
            )
        } catch (e: Exception) {
            Log.e("PersonDataSource", e.message.toString())
        }
    }

    fun getPersonDetail(personId: Int) {
        try {
            compositeDisposable.add(
                apiService.getPersonDetail(personId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _personDetailResponse.postValue(it)
                    }, {
                        Log.e("PersonDataSource", it.message!!)
                    })
            )
        } catch (e: Exception) {
            Log.e("PersonDataSource", e.message.toString())
        }
    }
}