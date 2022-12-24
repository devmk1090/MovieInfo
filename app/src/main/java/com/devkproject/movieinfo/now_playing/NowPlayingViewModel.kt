package com.devkproject.movieinfo.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

class NowPlayingViewModel (private val apiService: TMDBInterface): ViewModel() {

    private lateinit var nowPlayingPagedList: LiveData<PagedList<TMDBThumb>>
    private lateinit var nowPlayingDataSourceFactory: NowPlayingDataSourceFactory
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

    fun nowPlayingNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<NowPlayingDataSource, NetworkState>(
            nowPlayingDataSourceFactory.nowPlayingLiveDataSource, NowPlayingDataSource::networkState)
    }

    fun listIsEmpty(): Boolean {
        return nowPlayingPagedList.value?.isEmpty()?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class NowPlayingViewModelFactory(private val apiService: TMDBInterface): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(NowPlayingViewModel::class.java)) {
                NowPlayingViewModel(apiService) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}