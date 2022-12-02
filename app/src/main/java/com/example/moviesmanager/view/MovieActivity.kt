package com.example.moviesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.moviesmanager.databinding.ActivityMovieBinding
import com.example.moviesmanager.model.Constant.EXTRA_MOVIE
import com.example.moviesmanager.model.Constant.INVALID_MOVIE_ID
import com.example.moviesmanager.model.Constant.VIEW_MOVIE
import com.example.moviesmanager.model.entity.Movie

class MovieActivity : AppCompatActivity() {
    private val acb: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        val receivedMovie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        receivedMovie?.let{ _receivedMovie ->
            with(acb) {
                with(_receivedMovie) {
                    nameEt.setText(name)
                    addressEt.setText(address)
                    phoneEt.setText(phone)
                    emailEt.setText(email)
                }
            }
        }
        val viewMovie = intent.getBooleanExtra(VIEW_MOVIE, false)
        if (viewMovie) {
            acb.nameEt.isEnabled = false
            acb.addressEt.isEnabled = false
            acb.phoneEt.isEnabled = false
            acb.emailEt.isEnabled = false
            acb.saveBt.visibility = View.GONE
        }

        acb.saveBt.setOnClickListener {
            val movie = Movie(
                id = receivedMovie?.id,
                name = acb.nameEt.text.toString(),
                address = acb.addressEt.text.toString(),
                phone = acb.phoneEt.text.toString(),
                email = acb.emailEt.text.toString(),
            )
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_MOVIE, movie)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}