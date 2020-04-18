package com.devkproject.movieinfo.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class NowPlayingViewModel (private val apiService: TMDBInterface): ViewModel() {

    lateinit var nowPlayingPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var nowPlayingDataSourceFactory: NowPlayingDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    fun nowPlayingView(): LiveData<PagedList<TMDBThumb>> {
        nowPlayingDataSourceFactory = NowPlayingDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        nowPlayingPagedList = LivePagedListBuilder(nowPlayingDataSourceFactory, config).build()
        return nowPlayingPagedList
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}