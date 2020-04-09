package com.devkproject.movieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devkproject.movieinfo.model.TMDBThumb
import com.devkproject.movieinfo.paging.TMDBPagedListRepository

class MainViewModel (private val tmdbPagedListRepository: TMDBPagedListRepository): ViewModel() {

    val tmdbPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
        tmdbPagedListRepository.getTMDBPagedList()
    }

    fun listIsEmpty(): Boolean {
        return tmdbPagedList.value?.isEmpty()?: true
    }

    override fun onCleared() {
        super.onCleared()
    }
}