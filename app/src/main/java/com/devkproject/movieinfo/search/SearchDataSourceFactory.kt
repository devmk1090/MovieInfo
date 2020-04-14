package com.devkproject.movieinfo.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable, private val searchQuery: String)
    : DataSource.Factory<Int, TMDBThumb>() {

    private val searchLiveDataSource = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val searchDataSource = SearchDataSource(apiService, compositeDisposable, searchQuery)
        searchLiveDataSource.postValue(searchDataSource)
        return searchDataSource
    }
}