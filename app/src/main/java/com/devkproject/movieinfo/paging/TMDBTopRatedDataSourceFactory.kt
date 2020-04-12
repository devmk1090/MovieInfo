package com.devkproject.movieinfo.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class TMDBTopRatedDataSourceFactory (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable)
    :DataSource.Factory<Int, TMDBThumb>() {

    private val topRatedLiveDataSource = MutableLiveData<TMDBTopRatedDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val topRatedDataSource = TMDBTopRatedDataSource(apiService, compositeDisposable)
        topRatedLiveDataSource.postValue(topRatedDataSource)
        return topRatedDataSource
    }

}