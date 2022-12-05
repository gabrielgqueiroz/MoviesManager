package com.example.moviesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.moviesmanager.R
import android.R.layout.simple_spinner_item
import android.R.layout.simple_spinner_dropdown_item
import android.widget.Toast
import com.example.moviesmanager.databinding.ActivityMovieBinding
import com.example.moviesmanager.model.Constant.EXTRA_MOVIE
import com.example.moviesmanager.model.Constant.VIEW_MOVIE
import com.example.moviesmanager.model.entity.Genres
import com.example.moviesmanager.model.entity.Movie
import java.time.Year
import java.util.*

class MovieActivity : AppCompatActivity() {
    private val amb: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        val arrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, Genres.values()
        )
        arrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item)
        amb.genresSp.adapter = arrayAdapter

        val receivedMovie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        receivedMovie?.let{ _receivedMovie ->
            with(amb) {
                with(_receivedMovie) {
                    Log.v("teste", arrayAdapter.getPosition(Genres.ROMANCE).toString())
                    nameEt.setText(name)
                    yearEt.setText(year.toString())
                    producerEt.setText(producer)
                    durationInMinutesEt.setText(durationInMinutes)
                    flagWatchedRg.check(
                        if (watched) amb.watchedRb.id else amb.notWatchedRb.id
                    )
                    ratingSp.setSelection(ratingSp.adapter.getItem(rating).toString().toInt())
                    genresSp.setSelection(arrayAdapter.getPosition(genre))
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
            var emptyFields = ""
            var invalidYear = false
            var tooLongDuration = false
            // Optei por deixar a duração até 720 horas (43200 minutos) pois é a duração do filme mais longo até hoje

            with(amb){
                Log.v("teste", Date().toString())
                if (nameEt.text.isEmpty()) emptyFields += "Nome, "
                if (yearEt.text.isEmpty()) emptyFields += "Ano, "
                else if (yearEt.text.toString().toInt() > Date().year + 10)  invalidYear = true // adicionar data do filme mais antigo
                if (producerEt.text.isEmpty()) emptyFields += "Estúdio ou produtora, "
                if (durationInMinutesEt.text.isEmpty()) emptyFields += "Duração "
                else if (durationInMinutesEt.text.toString().toInt() > 43200) tooLongDuration = true
            }
            if (emptyFields != "") {
                Toast.makeText(
                    this, "Os campos: " + emptyFields + "estão vazios", Toast.LENGTH_LONG
                ).show()
            }
            else if (invalidYear){
                Toast.makeText(
                    this, "Data de lançamento inválida", Toast.LENGTH_LONG
                ).show()
            }
            else if (tooLongDuration){
                Toast.makeText(
                    this, "Duração do filme muito longo", Toast.LENGTH_LONG
                ).show()
            }
            else{
                val movie = Movie(
                    id = receivedMovie?.id,
                    name = amb.nameEt.text.toString(),
                    year = amb.yearEt.text.toString(),
                    producer = amb.producerEt.text.toString(),
                    durationInMinutes = amb.durationInMinutesEt.text.toString(),
                    watched = amb.watchedRb.isSelected,
                    rating = amb.ratingSp.selectedItem.toString().toInt(),
                    genre = Genres.valueOf(amb.genresSp.selectedItem.toString().uppercase())
                )
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_MOVIE, movie)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}