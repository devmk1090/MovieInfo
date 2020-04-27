package com.devkproject.movieinfo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteMovie")
data class Favorite (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id") val movieId: Int?
)