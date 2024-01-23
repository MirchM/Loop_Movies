package com.mmisoft.loop_movies.repository.network

import com.mmisoft.loop_movies.data.api.MovieService
import com.mmisoft.loop_movies.data.model.remote.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MovieRepository {
    suspend fun getAllMovies(): List<Movie>?

    suspend fun getStaffPick(): List<Movie>?
}