package com.devkproject.movieinfo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Query("SELECT * FROM favoriteMovie")
    fun getMovieList(): LiveData<List<Favorite>>

    @Delete
    fun delete(favorite: Favorite)

    @Query("DELETE FROM favoriteMovie")
    fun deleteAll()

}