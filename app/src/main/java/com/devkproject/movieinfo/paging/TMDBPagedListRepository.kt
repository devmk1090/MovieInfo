package com.devkproject.movieinfo.paging

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb

class TMDBPagedListRepository (private val apiService: TMDBInterface) {

    lateinit var tmdbPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var tmdbDataSourceFactory: TMDBDataSourceFactory

    fun getTMDBPagedList() : LiveData<PagedList<TMDBThumb>> {
        tmdbDataSourceFactory = TMDBDataSourceFactory(apiService)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false) //default: true
            .setPageSize(PER_PAGE) //default: page size * 3
            .build()
        tmdbPagedList = LivePagedListBuilder(tmdbDataSourceFactory, config).build()

        return tmdbPagedList
    }
}