package com.devkproject.movieinfo.now_playing

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class NowPlayingDataSourceFactory (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, TMDBThumb>() {

    private val nowPlayingLiveDataSource = MutableLiveData<NowPlayingDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val nowPlayingDataSource = NowPlayingDataSource(apiService, compositeDisposable)
        nowPlayingLiveDataSource.postValue(nowPlayingDataSource)
        return nowPlayingDataSource
    }
}