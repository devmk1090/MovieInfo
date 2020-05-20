package com.devkproject.movieinfo.detail.person

import androidx.lifecycle.LiveData
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBPerson
import io.reactivex.disposables.CompositeDisposable

class PersonRepository (private val apiService: TMDBInterface) {
    lateinit var personDataSource: PersonDataSource

    fun getPerson(compositeDisposable: CompositeDisposable, personId: Int): LiveData<TMDBPerson> {
        personDataSource = PersonDataSource(apiService, compositeDisposable)
        personDataSource.getPerson(personId)
        return personDataSource.personResponse
    }
}