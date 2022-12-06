package com.example.moviesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.R.layout.simple_spinner_dropdown_item
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import android.widget.Toast
import com.example.moviesmanager.R
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
                Genres.values()[it].genre },
            amb.genresSp,
            getString(R.string.chose_genre)
        )
        val arrayAdapterRating = addArrayToSpinner(
            List(11) { i -> i * 1}, amb.ratingSp, getString(R.string.rating_movie)
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
                    genresSp.setSelection(arrayAdapterGenres.getPosition(genre.genre))
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
            val emptyFields = ArrayList<String>()
            var invalidYear = false
            // Optei por deixar a duração até 720 horas (43200 minutos) pois é a duração
            // do filme mais longo até hoje
            var tooLongDuration = false


            with(amb){
                if (nameEt.text.isEmpty()) emptyFields.add(getString(R.string.name))
                if (yearEt.text.isEmpty()) emptyFields.add(getString(R.string.year))
                else if (yearEt.text.toString().toInt() > thisYear + 10 || yearEt.text.toString().toInt() < 1888)
                    // Roundhay Garden Scene de 1888 é considerado o filme mais antigo do mundo
                    invalidYear = true
                if (producerEt.text.isEmpty()) emptyFields.add(getString(R.string.studio_producer))
                if (durationInMinutesEt.text.isEmpty()) emptyFields.add(getString(R.string.duration))
                else if (durationInMinutesEt.text.toString().toInt() > 43200) tooLongDuration = true
                true
            }
            val moviesNames = intent.getStringArrayExtra(MOVIES_NAMES)
            val hasName = moviesNames?.find {
                it == amb.nameEt.text.toString().trim()
            }

            if (hasName != null){
                Toast.makeText(
                    this, getString(R.string.error_name_exists), Toast.LENGTH_LONG
                ).show()
            }
            else if (emptyFields.isNotEmpty()) {
                Toast.makeText(
                    this, "Os campos: " + emptyFields.joinToString(separator = ", ") + " estão vazios", Toast.LENGTH_LONG
                ).show()
            }
            else if (invalidYear){
                Toast.makeText(
                    this, getString(R.string.error_invalid_year), Toast.LENGTH_LONG
                ).show()
            }
            else if (tooLongDuration){
                Toast.makeText(
                    this, getString(R.string.error_invalid_duration), Toast.LENGTH_LONG
                ).show()
            }
            else if (amb.genresSp.selectedItemPosition == 0){
                Toast.makeText(
                    this, getString(R.string.error_chose_gender), Toast.LENGTH_LONG
                ).show()
            }
            else if (amb.ratingSp.selectedItemPosition == 0 ){
                Toast.makeText(
                    this, getString(R.string.error_rating_movie), Toast.LENGTH_LONG
                ).show()
            }
            else{

                val genreSelected = Genres.values().find { amb.genresSp.selectedItem.toString() == it.genre }

                val movie = Movie(
                    id = receivedMovie?.id,
                    name = amb.nameEt.text.toString().trim(),
                    year = amb.yearEt.text.toString(),
                    producer = amb.producerEt.text.toString(),
                    durationInMinutes = amb.durationInMinutesEt.text.toString(),
                    watched = amb.watchedRb.isChecked,
                    rating = amb.ratingSp.selectedItem.toString().toInt(),
                    genre = Genres.valueOf(genreSelected!!.name)
                )
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_MOVIE, movie)
                setResult(RESULT_OK, resultIntent)
                finish()

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_secondary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.closeMi -> {
                finish()
                true
            }
            else -> { false }
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