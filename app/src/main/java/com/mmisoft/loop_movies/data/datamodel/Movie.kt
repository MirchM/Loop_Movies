package com.mmisoft.loop_movies.data.datamodel

data class Movie(
    val rating: Double?,
    val id: Int?,
    val revenue: Int?,
    val releaseDate: String?,
    val director: MovieDirector?,
    val posterUrl: String?,
    val cast: List<MovieCast>?,
    val runtime: Int?,
    val title: String?,
    val overview: String?,
    val reviews: Int?,
    val budget: Int?,
    val language: String?,
    val genres: List<String>?
)
