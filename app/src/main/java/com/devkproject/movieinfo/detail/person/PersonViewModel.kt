package com.devkproject.movieinfo.detail.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devkproject.movieinfo.model.TMDBPerson
import com.devkproject.movieinfo.model.TMDBPersonDetail
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

class PersonViewModel(private val personRepository: PersonRepository, personId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val getPerson: LiveData<TMDBPerson> by lazy {
        personRepository.getPerson(compositeDisposable, personId)
    }

    val getPersonDetail: LiveData<TMDBPersonDetail> by lazy {
        personRepository.getPersonDetail(compositeDisposable, personId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class PersonViewModelFactory(private val personRepository: PersonRepository, private val personId: Int): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
                PersonViewModel(personRepository, personId) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}