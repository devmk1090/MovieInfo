package com.devkproject.movieinfo.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class PopularRepository (private val apiService: TMDBInterface) {

    lateinit var popularPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var popularDataSourceFactory: PopularDataSourceFactory

    fun getPopularPagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<TMDBThumb>> {
        popularDataSourceFactory =
            PopularDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false) //default: true
            .setPageSize(PER_PAGE) //default: page size * 3
            .build()
        popularPagedList = LivePagedListBuilder(popularDataSourceFactory, config).build()

        return popularPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<PopularDataSource, NetworkState>(
            popularDataSourceFactory.popularLiveDataSource, PopularDataSource::networkState)
    }
}