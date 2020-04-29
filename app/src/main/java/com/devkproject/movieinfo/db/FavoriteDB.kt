package com.devkproject.movieinfo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteDB: RoomDatabase() {

    abstract fun favoriteDAO(): FavoriteDAO

    private class FavoriteDBCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE.let { database ->
                scope.launch {
                    var favoriteDAO = database!!.favoriteDAO()
                    favoriteDAO.deleteAll()
                }
            }
        }
    }
    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: FavoriteDB? = null

        fun getDatabase(context: Context): FavoriteDB? {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDB::class.java,
                    "favorite_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}