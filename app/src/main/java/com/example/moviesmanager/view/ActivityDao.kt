package com.example.moviesmanager.view

import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.model.entity.Movie

abstract class ActivityDao: AppCompatActivity() {
    abstract fun updateMovieList(_movieList: MutableList<Movie>)
}