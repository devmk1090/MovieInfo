package com.devkproject.movieinfo.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class UpcomingDataSourceFactory (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable)
    :DataSource.Factory<Int, TMDBThumb>() {

    val upcomingLiveDataSource = MutableLiveData<UpcomingDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val upcomingDataSource = UpcomingDataSource(apiService, compositeDisposable)
        upcomingLiveDataSource.postValue(upcomingDataSource)
        return upcomingDataSource
    }
}