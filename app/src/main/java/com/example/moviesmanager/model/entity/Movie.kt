package com.example.moviesmanager.model.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.Year

@Parcelize
@Entity
data class Movie(
    @PrimaryKey
    var name: String,
    @NonNull
    var year: Year,
    @NonNull
    var producer: String,
    @NonNull
    var durationInMinutes: String,
    @NonNull
    var watched: Boolean,
    @NonNull
    var rating: Int,
    @NonNull
    var genre: Enum<Genre>,
): Parcelable
