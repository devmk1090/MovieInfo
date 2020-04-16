//package com.devkproject.movieinfo.search
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.paging.LivePagedListBuilder
//import androidx.paging.PagedList
//import com.devkproject.movieinfo.api.PER_PAGE
//import com.devkproject.movieinfo.api.TMDBInterface
//import com.devkproject.movieinfo.model.TMDBThumb
//import io.reactivex.disposables.CompositeDisposable
//
//class SearchRepository(private val apiService: TMDBInterface) {
//
//    lateinit var searchPagedList: LiveData<PagedList<TMDBThumb>>
//    lateinit var searchDataSourceFactory: SearchDataSourceFactory
//
//    fun getSearchPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<TMDBThumb>> {
//        Log.e("SearchRepository", "SearchRepository : $")
//        searchDataSourceFactory = SearchDataSourceFactory(apiService, compositeDisposable)
//
//        val config: PagedList.Config = PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setPageSize(PER_PAGE)
//            .build()
//        searchPagedList = LivePagedListBuilder(searchDataSourceFactory, config).build()
//
//        return searchPagedList
//    }
//}