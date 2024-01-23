package com.mmisoft.loop_movies.data.model.local

import com.mmisoft.loop_movies.data.model.remote.Movie

data class User(
    val name : String,
    val favouriteMovies: List<Int>
)