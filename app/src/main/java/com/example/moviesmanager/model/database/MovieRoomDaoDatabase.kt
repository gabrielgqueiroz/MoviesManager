package com.example.moviesmanager.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesmanager.model.dao.MovieRoomDao
import com.example.moviesmanager.model.entity.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieRoomDaoDatabase: RoomDatabase() {
    abstract fun getMovieRoomDao(): MovieRoomDao
}