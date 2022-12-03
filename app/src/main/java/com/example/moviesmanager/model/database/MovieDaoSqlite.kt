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
import java.sql.SQLException

class MovieDaoSqlite(context: Context) : MovieDao {
    companion object Constant {
        private const val MOVIE_DATABASE_FILE = "movies"
        private const val MOVIE_TABLE = "movie"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val ADDRESS_COLUMN = "address"
        private const val PHONE_COLUMN = "phone"
        private const val EMAIL_COLUMN = "email"
        private const val LASTNAME_COLUMN = "lastname"

        private const val CREATE_MOVIE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $MOVIE_TABLE ( " +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$ADDRESS_COLUMN TEXT NOT NULL, " +
                    "$PHONE_COLUMN TEXT NOT NULL, " +
                    "$EMAIL_COLUMN TEXT NOT NULL );"
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
        put(ADDRESS_COLUMN, address)
        put(PHONE_COLUMN, phone)
        put(EMAIL_COLUMN, email)
        this
    }

    private fun movieToContentValues(movie: Movie) = with(ContentValues()) {
        put(NAME_COLUMN, movie.name)
        put(ADDRESS_COLUMN, movie.address)
        put(PHONE_COLUMN, movie.phone)
        put(EMAIL_COLUMN, movie.email)
        this
    }

    private fun Cursor.rowToMovie() = Movie(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(NAME_COLUMN)),
        getString(getColumnIndexOrThrow(ADDRESS_COLUMN)),
        getString(getColumnIndexOrThrow(PHONE_COLUMN)),
        getString(getColumnIndexOrThrow(EMAIL_COLUMN)),
        getString(getColumnIndexOrThrow(LASTNAME_COLUMN))
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