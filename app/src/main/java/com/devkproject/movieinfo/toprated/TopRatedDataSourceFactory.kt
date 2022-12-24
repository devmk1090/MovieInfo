package com.devkproject.movieinfo.toprated

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class TopRatedDataSourceFactory (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable)
    :DataSource.Factory<Int, TMDBThumb>() {

    val topRatedLiveDataSource = MutableLiveData<TopRatedDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val topRatedDataSource = TopRatedDataSource(apiService, compositeDisposable)
        topRatedLiveDataSource.postValue(topRatedDataSource)
        return topRatedDataSource
    }

}