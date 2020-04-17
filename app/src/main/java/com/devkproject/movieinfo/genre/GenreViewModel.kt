package com.devkproject.movieinfo.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class GenreViewModel (private val apiService: TMDBInterface): ViewModel() {

    lateinit var genrePagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var genreDataSourceFactory: GenreDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    fun getGenreView(genreId: String): LiveData<PagedList<TMDBThumb>> {
        genreDataSourceFactory = GenreDataSourceFactory(apiService, compositeDisposable, genreId)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        genrePagedList = LivePagedListBuilder(genreDataSourceFactory, config).build()
        return genrePagedList
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}