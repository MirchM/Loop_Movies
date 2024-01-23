package com.mmisoft.loop_movies.ui.recyclerview


interface RecyclerViewClickListener {
    fun onMovieClick(movieId: Int)

    fun onBookmarkClick(movieId: Int)
}