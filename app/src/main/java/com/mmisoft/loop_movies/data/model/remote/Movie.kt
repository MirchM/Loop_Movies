package com.mmisoft.loop_movies.data.model.remote

data class Movie(
    val rating: Double,
    val id: Int,
    val revenue: Long,
    val releaseDate: String,
    val director: Director,
    val posterUrl: String,
    val cast: List<Cast>,
    val runtime: Int,
    val title: String,
    val overview: String,
    val reviews: Int,
    val budget: Long,
    val language: String,
    val genres: List<String>,
)
