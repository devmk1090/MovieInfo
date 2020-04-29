package com.devkproject.movieinfo.db

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class FavoriteRepository (private val favoriteDAO: FavoriteDAO) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allMovie: LiveData<List<Favorite>> = favoriteDAO.getMovieList()


    fun insert(favorite: Favorite) {
        favoriteDAO.insert(favorite)
    }
    fun delete(favorite: Favorite) {
        favoriteDAO.delete(favorite)
    }
}