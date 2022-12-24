package com.devkproject.movieinfo.detail.person

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBPerson
import com.devkproject.movieinfo.model.TMDBPersonDetail
import io.reactivex.disposables.CompositeDisposable

class PersonRepository (private val apiService: TMDBInterface) {

    private lateinit var personDataSource: PersonDataSource

    fun getPerson(compositeDisposable: CompositeDisposable, personId: Int): LiveData<TMDBPerson> {
        personDataSource = PersonDataSource(apiService, compositeDisposable)
        personDataSource.getPerson(personId)
        return personDataSource.personResponse
    }

    fun getPersonDetail(compositeDisposable: CompositeDisposable, personId: Int): LiveData<TMDBPersonDetail> {
        personDataSource = PersonDataSource(apiService, compositeDisposable)
        personDataSource.getPersonDetail(personId)
        return personDataSource.personDetailResponse
    }
}