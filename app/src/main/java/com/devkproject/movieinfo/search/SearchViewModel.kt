package com.devkproject.movieinfo.search

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

class SearchViewModel (private val apiService: TMDBInterface): ViewModel() {

    private lateinit var searchPagedList: LiveData<PagedList<TMDBThumb>>
    private lateinit var searchDataSourceFactory: SearchDataSourceFactory
    private val compositeDisposable = CompositeDisposable()


    fun searchView(searchQuery: String): LiveData<PagedList<TMDBThumb>> {
        searchDataSourceFactory =
            SearchDataSourceFactory(apiService, compositeDisposable, searchQuery)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        searchPagedList = LivePagedListBuilder(searchDataSourceFactory, config).build()
        return searchPagedList
    }

    fun searchViewNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<SearchDataSource, NetworkState>(
            searchDataSourceFactory.searchLiveDataSource, SearchDataSource::networkState)
    }

    fun listIsEmpty():Boolean {
        return searchPagedList.value?.isEmpty()?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    class SearchViewModelFactory(private val apiService: TMDBInterface): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                SearchViewModel(apiService) as T
            } else {
                throw IllegalArgumentException()
            }
        }

    }
}
