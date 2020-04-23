package com.devkproject.movieinfo.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class GenreViewModel (private val apiService: TMDBInterface): ViewModel() {

    lateinit var genrePagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var genreDataSourceFactory: GenreDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    fun getGenreView(genreId: String, sort_by: String): LiveData<PagedList<TMDBThumb>> {
        genreDataSourceFactory = GenreDataSourceFactory(apiService, compositeDisposable, genreId, sort_by)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        genrePagedList = LivePagedListBuilder(genreDataSourceFactory, config).build()
        return genrePagedList
    }

    fun genreNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<GenreDataSource, NetworkState>(
            genreDataSourceFactory.genreLiveDataSource, GenreDataSource::networkState)
    }

    fun listIsEmpty(): Boolean {
        return genrePagedList.value?.isEmpty()?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}