package com.devkproject.movieinfo.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class UpcomingRepository (private val apiService: TMDBInterface) {

    lateinit var upcomingPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var upcomingDataSourceFactory: UpcomingDataSourceFactory

    fun getUpcomingPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<TMDBThumb>> {
        upcomingDataSourceFactory = UpcomingDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        upcomingPagedList = LivePagedListBuilder(upcomingDataSourceFactory, config).build()

        return upcomingPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<UpcomingDataSource, NetworkState>(
            upcomingDataSourceFactory.upcomingLiveDataSource, UpcomingDataSource::networkState)
    }
}