package com.devkproject.movieinfo.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb

class TMDBDataSourceFactory (private val apiService: TMDBInterface): DataSource.Factory<Int, TMDBThumb>() {

    private val movieLiveDataSource = MutableLiveData<TMDBDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val movieDataSource = TMDBDataSource(apiService)

        movieLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}