package com.devkproject.movieinfo.toprated

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.FIRST_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TopRatedDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable): PageKeyedDataSource<Int, TMDBThumb>() {

    private val page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TMDBThumb>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getTopRatedMovie(page, "kr")
                .subscribeOn(Schedulers.io())
                .subscribe( {
                    callback.onResult(it.movieList, null, page + 1)
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    Log.e("TopRatedDataSource", it.message!!)
                    networkState.postValue(NetworkState.ERROR)
                }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TMDBThumb>) {
        compositeDisposable.add(
            apiService.getTopRatedMovie(params.key, "kr")
                .subscribeOn(Schedulers.io())
                .subscribe( {
                    if(it.totalPages >= params.key) {
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(it.movieList, params.key + 1)
                    } else {
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    Log.e("TopRatedDataSource", it.message!!)
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TMDBThumb>) {}
}