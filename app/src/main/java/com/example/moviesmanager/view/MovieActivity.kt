package com.example.moviesmanager.view

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.moviesmanager.databinding.ActivityMovieBinding
import com.example.moviesmanager.model.Constant.EXTRA_MOVIE
import com.example.moviesmanager.model.Constant.VIEW_MOVIE
import com.example.moviesmanager.model.entity.Genres
import com.example.moviesmanager.model.entity.Movie
import java.time.Year

class MovieActivity : AppCompatActivity() {
    private val amb: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        /*ArrayAdapter.createFromResource(
            this,
            R.array.genre,
            R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            amb.genresSp.adapter = adapter
        }*/

        val receivedMovie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        receivedMovie?.let{ _receivedMovie ->
            with(amb) {
                with(_receivedMovie) {
                    Log.v("teste", this.toString())
                    nameEt.setText(name)
                    yearEt.setText(year.toString())
                    producerEt.setText(producer)
                    durationInMinutesEt.setText(durationInMinutes)
                    flagWatchedRg.check(
                        if (watched) amb.watchedRb.id else amb.notWatchedRb.id
                    )
                    ratingSp.prompt = rating.toString()
                    genresSp.prompt = genre.toString()
                }
            }
        }
        val viewMovie = intent.getBooleanExtra(VIEW_MOVIE, false)
        if (viewMovie) {
            amb.nameEt.isEnabled = false
            amb.yearEt.isEnabled = false
            amb.producerEt.isEnabled = false
            amb.durationInMinutesEt.isEnabled = false
            amb.watchedRb.isEnabled = false
            amb.notWatchedRb.isEnabled = false
            amb.ratingSp.isEnabled = false
            amb.genresSp.isEnabled = false
            amb.saveBt.visibility = View.GONE
        }

        amb.saveBt.setOnClickListener {
            val movie = Movie(
                id = receivedMovie?.id,
                name = amb.nameEt.text.toString(),
                year = amb.yearEt.text.toString(),
                producer = amb.producerEt.text.toString(),
                durationInMinutes = amb.durationInMinutesEt.text.toString(),
                watched = amb.watchedRb.isSelected,
                rating = amb.ratingSp.selectedItem.toString().toInt(),
                genre = Genres.valueOf(amb.genresSp.selectedItem.toString().toUpperCase())
            )
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_MOVIE, movie)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}