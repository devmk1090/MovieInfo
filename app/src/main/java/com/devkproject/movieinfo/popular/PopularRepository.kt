package com.devkproject.movieinfo.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.api.PER_PAGE
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class PopularRepository (private val apiService: TMDBInterface) {

    lateinit var popularPagedList: LiveData<PagedList<TMDBThumb>>
    lateinit var popularDataSourceFactory: PopularDataSourceFactory

    fun getPopularPagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<TMDBThumb>> {
        popularDataSourceFactory =
            PopularDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false) //default: true
            .setPageSize(PER_PAGE) //default: page size * 3
            .build()
        popularPagedList = LivePagedListBuilder(popularDataSourceFactory, config).build()

        return popularPagedList
    }


    //첫번째 인자로 LiveData source 를 넘겨준다. 넘겨준 LiveData source 가 변경될 때마다 switchMap 이 반환하는 새로운 LiveData 의 value 역시 새롭게 갱신됨.
    //두번째 인자는 함수를 넘겨준다
    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<PopularDataSource, NetworkState>(
            popularDataSourceFactory.popularLiveDataSource, PopularDataSource::networkState)
    }
}