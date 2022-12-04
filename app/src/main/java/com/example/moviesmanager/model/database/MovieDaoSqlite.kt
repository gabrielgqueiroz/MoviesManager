package com.example.moviesmanager.model.database

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.moviesmanager.R
import com.example.moviesmanager.model.entity.Movie
import com.example.moviesmanager.model.dao.MovieDao
import com.example.moviesmanager.model.entity.Genres
import java.sql.SQLException

class MovieDaoSqlite(context: Context) : MovieDao {
    companion object Constant {
        private const val MOVIE_DATABASE_FILE = "movies"
        private const val MOVIE_TABLE = "movie"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val YEAR_COLUMN = "year"
        private const val PRODUCER_COLUMN = "producer"
        private const val DURATION_COLUMN = "duration"
        private const val WATCHED_COLUMN = "watched"
        private const val RATING_COLUMN = "rating"
        private const val GENRE_COLUMN = "genre"

        private const val CREATE_MOVIE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $MOVIE_TABLE ( " +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$YEAR_COLUMN TEXT NOT NULL, " +
                    "$PRODUCER_COLUMN TEXT NOT NULL, " +
                    "$DURATION_COLUMN TEXT NOT NULL );"
    }

    // Referência para o banco de dados
    private val movieSqliteDatabase: SQLiteDatabase

    init {
        // Criando ou abrindo o banco
        movieSqliteDatabase = context.openOrCreateDatabase(
            MOVIE_DATABASE_FILE,
            MODE_PRIVATE,
            null
        )
        try {
            movieSqliteDatabase.execSQL(CREATE_MOVIE_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    private fun Movie.toContentValues() = with(ContentValues()) {
        put(NAME_COLUMN, name)
        put(YEAR_COLUMN, year)
        put(PRODUCER_COLUMN, producer)
        put(DURATION_COLUMN, durationInMinutes)
        this
    }

    private fun movieToContentValues(movie: Movie) = with(ContentValues()) {
        put(NAME_COLUMN, movie.name)
        put(YEAR_COLUMN, movie.year)
        put(PRODUCER_COLUMN, movie.producer)
        put(DURATION_COLUMN, movie.durationInMinutes)
        this
    }

    private fun Cursor.rowToMovie() = Movie(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(NAME_COLUMN)),
        getString(getColumnIndexOrThrow(YEAR_COLUMN)),
        getString(getColumnIndexOrThrow(PRODUCER_COLUMN)),
        getString(getColumnIndexOrThrow(DURATION_COLUMN)),
        getInt(getColumnIndexOrThrow(WATCHED_COLUMN)) == 1,
        getInt(getColumnIndexOrThrow(RATING_COLUMN)),
        Genres.valueOf(getString(getColumnIndexOrThrow(GENRE_COLUMN)))
    )

    override fun createMovie(movie: Movie) = movieSqliteDatabase.insert(
        MOVIE_TABLE,
        null,
        movieToContentValues(movie)
    ).toInt()


    override fun retrieveMovie(id: Int): Movie? {
        val cursor = movieSqliteDatabase.rawQuery(
            "SELECT * FROM $MOVIE_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )
        val movie = if (cursor.moveToFirst()) cursor.rowToMovie() else null

        cursor.close()
        return movie
    }

    override fun retrieveMovies(): MutableList<Movie> {
        val movieList = mutableListOf<Movie>()
        val cursor = movieSqliteDatabase.rawQuery(
            "SELECT * FROM $MOVIE_TABLE ORDER BY $NAME_COLUMN",
            null
        )
        while (cursor.moveToNext()) {
            movieList.add(cursor.rowToMovie())
        }
        cursor.close()
        return movieList
    }

    override fun updateMovie(movie: Movie) = movieSqliteDatabase.update(
        MOVIE_TABLE,
        movie.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(movie.id.toString())
    )

    override fun deleteMovie(id: Int) =
        movieSqliteDatabase.delete(
            MOVIE_TABLE,
            "$ID_COLUMN = ?",
            arrayOf(id.toString())
        )
}