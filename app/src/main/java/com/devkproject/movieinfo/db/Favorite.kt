package com.devkproject.movieinfo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteMovie")
data class Favorite (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id") val movieId: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "release") val release: String?,
    @ColumnInfo(name = "rating") val rating: Double?,
    @ColumnInfo(name = "poster") val poster: String?
)