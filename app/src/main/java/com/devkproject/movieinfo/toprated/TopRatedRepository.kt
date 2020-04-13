package com.devkproject.movieinfo.toprated

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class TopRatedRepository (private val apiService: TMDBInterface) {

    lateinit var tmdbTopRatedPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var tmdbTopRatedDataSourceFactory: TopRatedDataSourceFactory

    fun getTMDBTopRatedPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<TMDBThumb>> {
        tmdbTopRatedDataSourceFactory =
            TopRatedDataSourceFactory(
                apiService,
                compositeDisposable
            )

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        tmdbTopRatedPagedList = LivePagedListBuilder(tmdbTopRatedDataSourceFactory, config).build()

        return tmdbTopRatedPagedList
    }
}