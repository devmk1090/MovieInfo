package com.devkproject.movieinfo.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class SearchViewModel (private var apiService: TMDBInterface): ViewModel() {

    lateinit var searchPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var searchDataSourceFactory: SearchDataSourceFactory
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


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

//val searchPagedList: LiveData<PagedList<TMDBThumb>> by lazy {
//    searchRepository.getSearchPagedList(compositeDisposable, searchQuery)
//}
