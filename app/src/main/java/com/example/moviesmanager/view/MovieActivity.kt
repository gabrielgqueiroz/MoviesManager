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
import android.widget.Spinner
import android.widget.Toast
import com.example.moviesmanager.controller.MovieRoomController
import com.example.moviesmanager.databinding.ActivityMovieBinding
import com.example.moviesmanager.model.Constant.EXTRA_MOVIE
import com.example.moviesmanager.model.Constant.MOVIES_NAMES
import com.example.moviesmanager.model.Constant.VIEW_MOVIE
import com.example.moviesmanager.model.entity.Genres
import com.example.moviesmanager.model.entity.Movie
import java.util.*
import kotlin.collections.ArrayList

class MovieActivity : AppCompatActivity() {
    private val amb: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        val arrayAdapterGenres = addArrayToSpinner(
            List(Genres.values().size){
                Genres.valueOf(Genres.values()[it].toString().capitalize()) },
            amb.genresSp,
            "Escolha um Gênero:"
        )

        val arrayAdapterRating = addArrayToSpinner(
            List(11) { i -> i * 1}, amb.ratingSp, "Dê uma nota: "
        )
        // Tive que fazer isso porque o Date().year retornava 122 e outros como o
        // LocalDate fuciona só a partir da api 26
        val date = Date().toString()
        val thisYear = date.substring(date.length-4, date.length).toInt()

        val receivedMovie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        receivedMovie?.let{ _receivedMovie ->
            with(amb) {
                with(_receivedMovie) {
                    nameEt.setText(name)
                    nameEt.isEnabled = false
                    yearEt.setText(year)
                    producerEt.setText(producer)
                    durationInMinutesEt.setText(durationInMinutes)
                    flagWatchedRg.check(
                        if (watched) amb.watchedRb.id else amb.notWatchedRb.id
                    )
                    ratingSp.setSelection(arrayAdapterRating.getPosition(rating))
                    genresSp.setSelection(arrayAdapterGenres.getPosition(genre))
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
            var emptyFields = ArrayList<String>()
            var invalidYear = false
            var tooLongDuration = false

            // Optei por deixar a duração até 720 horas (43200 minutos) pois é a duração
            // do filme mais longo até hoje

            with(amb){
                if (nameEt.text.isEmpty()) emptyFields.add("Nome")
                if (yearEt.text.isEmpty()) emptyFields.add("Ano")
                else if (yearEt.text.toString().toInt() > thisYear + 10 || yearEt.text.toString().toInt() < 1888)
                    // Roundhay Garden Scene de 1888 é considerado o filme mais antigo do mundo
                    invalidYear = true
                if (producerEt.text.isEmpty()) emptyFields.add("Estúdio ou produtora")
                if (durationInMinutesEt.text.isEmpty()) emptyFields.add("Duração")
                else if (durationInMinutesEt.text.toString().toInt() > 43200) tooLongDuration = true
                true
            }
            if (emptyFields.isNotEmpty()) {
                Toast.makeText(
                    this, "Os campos: " + emptyFields.joinToString(separator = ", ") + " estão vazios", Toast.LENGTH_LONG
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
            else if (amb.genresSp.selectedItemPosition == 0){
                Toast.makeText(
                    this, "Por favor escolha um gênero", Toast.LENGTH_LONG
                ).show()
            }
            else if (amb.ratingSp.selectedItemPosition == 0 ){
                Toast.makeText(
                    this, "Por favor avalie o filme", Toast.LENGTH_LONG
                ).show()
            }
            else{
                val moviesNames = intent.getStringArrayExtra(MOVIES_NAMES)
                val hasName = moviesNames?.find {
                    it == amb.nameEt.text.toString()
                }
                if (hasName == null) {
                    val movie = Movie(
                        id = receivedMovie?.id,
                        name = amb.nameEt.text.toString(),
                        year = amb.yearEt.text.toString(),
                        producer = amb.producerEt.text.toString(),
                        durationInMinutes = amb.durationInMinutesEt.text.toString(),
                        watched = amb.watchedRb.isChecked,
                        rating = amb.ratingSp.selectedItem.toString().toInt(),
                        genre = Genres.valueOf(amb.genresSp.selectedItem.toString().uppercase())
                    )
                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_MOVIE, movie)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
                else {
                    Toast.makeText(
                        this, "Nome já cadastrado", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun addArrayToSpinner(
        list: List<Any?>, spinner: Spinner, hint: String = ""
    ): ArrayAdapter<Any?> {
        var listToAdd = list
        if (hint != "") {
            listToAdd = listOf(hint) + list
        }
        val arrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, listToAdd
        )
        arrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        return arrayAdapter
    }

}