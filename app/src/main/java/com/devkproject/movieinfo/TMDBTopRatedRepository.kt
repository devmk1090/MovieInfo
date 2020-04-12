package com.devkproject.movieinfo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import com.devkproject.movieinfo.paging.TMDBTopRatedDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class TMDBTopRatedRepository (private val apiService: TMDBInterface) {

    lateinit var tmdbTopRatedPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var tmdbTopRatedDataSourceFactory: TMDBTopRatedDataSourceFactory

    fun getTMDBTopRatedPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<TMDBThumb>> {
        tmdbTopRatedDataSourceFactory = TMDBTopRatedDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        tmdbTopRatedPagedList = LivePagedListBuilder(tmdbTopRatedDataSourceFactory, config).build()

        return tmdbTopRatedPagedList
    }
}