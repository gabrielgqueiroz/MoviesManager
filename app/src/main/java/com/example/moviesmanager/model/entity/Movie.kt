package com.example.moviesmanager.model.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(indices = [Index(value = ["name"], unique = true)])
data class Movie(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @NonNull
    val name: String,
    @NonNull
    val year: String,
    @NonNull
    val producer: String,
    @NonNull
    val durationInMinutes: String,
    @NonNull
    val watched: Boolean,
    @NonNull
    val rating: Int,
    @NonNull
    val genre: Genres,
): Parcelable
