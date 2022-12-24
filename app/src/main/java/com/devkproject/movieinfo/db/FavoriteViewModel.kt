package com.devkproject.movieinfo.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel (application: Application): AndroidViewModel(application) {

    private val favoriteRepository: FavoriteRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel
    val allMovie: LiveData<List<Favorite>>

    init {
        val favoriteDAO = FavoriteDB.getDatabase(application)!!.favoriteDAO()
        favoriteRepository = FavoriteRepository(favoriteDAO)
        allMovie = favoriteRepository.allMovie
    }

    // Launching a new coroutine to insert the data in a non-blocking way
    // LiveData를 사용할 때 값을 비동기적으로 계산해야 할 수 있습니다.
    // 예를 들어 사용자의 환경설정을 검색하여 UI에 제공하려고 할 수 있습니다.
    // 이러한 경우 liveData 빌더 함수를 사용해 suspend 함수를 호출하여 그 결과를 LiveData 개체로 제공할 수 있습니다.
    fun insert(favorite: Favorite) = viewModelScope.launch(Dispatchers.IO) {
        favoriteRepository.insert(favorite)
    }
    fun delete(favorite: Favorite) = viewModelScope.launch(Dispatchers.IO) {
        favoriteRepository.delete(favorite)
    }
}