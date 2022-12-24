package com.devkproject.movieinfo.genre

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.FIRST_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GenreDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable, private val genreId: String,
                       private val sort_by: String) : PageKeyedDataSource<Int, TMDBThumb>() {

    private val page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TMDBThumb>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getGenrePopularMovie("kr", sort_by, false, page, genreId, 100)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(it.movieList, null, page + 1)
                }, {
                    Log.e("GenreDataSource", it.message!!)
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TMDBThumb>) {
        compositeDisposable.add(
            apiService.getGenrePopularMovie("kr", sort_by, false, params.key, genreId, 100)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.totalPages >= params.key) {
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(it.movieList, params.key + 1)
                    } else {
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    Log.e("GenreDataSource", it.message!!)
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TMDBThumb>) {}
}