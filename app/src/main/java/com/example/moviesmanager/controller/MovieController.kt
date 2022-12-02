package com.example.moviesmanager.controller

import com.example.moviesmanager.model.entity.Movie
import com.example.moviesmanager.model.dao.MovieDao
import com.example.moviesmanager.model.database.MovieDaoSqlite
import com.example.moviesmanager.view.MainActivity

class MovieController(private val mainActivity: MainActivity) {
    private val movieDaoImpl: MovieDao = MovieDaoSqlite(mainActivity)

    fun insertMovie(movie: Movie) = movieDaoImpl.createMovie(movie)
    fun getMovie(id: Int) = movieDaoImpl.retrieveMovie(id)
    fun getMovies() {
        Thread {
                val returnedList = movieDaoImpl.retrieveMovies()
                mainActivity.updateMovieList(returnedList)
        }.start()
    }
    fun editMovie(movie: Movie) = movieDaoImpl.updateMovie(movie)
    fun removeMovie(id: Int) = movieDaoImpl.deleteMovie(id)
}