package com.devkproject.movieinfo.toprated

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class TopRatedRepository (private val apiService: TMDBInterface) {

    lateinit var topRatedPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var topRatedDataSourceFactory: TopRatedDataSourceFactory

    fun getTMDBTopRatedPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<TMDBThumb>> {
        topRatedDataSourceFactory =
            TopRatedDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        topRatedPagedList = LivePagedListBuilder(topRatedDataSourceFactory, config).build()

        return topRatedPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<TopRatedDataSource, NetworkState>(
            topRatedDataSourceFactory.topRatedLiveDataSource, TopRatedDataSource::networkState)
    }
}