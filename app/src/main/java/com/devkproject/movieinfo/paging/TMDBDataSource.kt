package com.devkproject.movieinfo.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.devkproject.movieinfo.api.FIRST_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TMDBDataSource (private val apiService: TMDBInterface, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, TMDBThumb>() {

    private val page = FIRST_PAGE
    private val networkState: MutableLiveData<String> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TMDBThumb>) {
        compositeDisposable.add(
        apiService.getPopularMovie(page)
            .subscribeOn(Schedulers.io())
            .subscribe( {
                callback.onResult(it.movieList, null, page + 1)
            }, {
                Log.e("TMDBDataSource", it.message)
                networkState.postValue(it.toString())
            })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TMDBThumb>) {
        compositeDisposable.add(
        apiService.getPopularMovie(params.key)
            .subscribeOn(Schedulers.io())
            .subscribe( {
                if(it.totalPages >= params.key) {
                    callback.onResult(it.movieList, params.key + 1)
                } else {
                    networkState.postValue("마지막 페이지")
                }
            }, {
                Log.e("TMDBDataSource", it.message)
                networkState.postValue(it.toString())
            }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TMDBThumb>) {

    }

}