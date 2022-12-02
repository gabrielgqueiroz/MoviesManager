package com.example.moviesmanager.model.dao

import com.example.moviesmanager.model.entity.Movie

interface MovieDao {
    fun createMovie(movie: Movie): Int
    fun retrieveMovie(id: Int): Movie?
    fun retrieveMovies(): MutableList<Movie>
    fun updateMovie(movie: Movie): Int
    fun deleteMovie(id: Int): Int
}