package com.devkproject.movieinfo.popular

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class PopularDataSourceFactory (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, TMDBThumb>() {

    private val movieLiveDataSource = MutableLiveData<PopularDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val movieDataSource = PopularDataSource(apiService, compositeDisposable)
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}