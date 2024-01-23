package com.mmisoft.loop_movies.repository.network

import android.util.Log
import com.google.gson.Gson
import com.mmisoft.loop_movies.data.api.MovieService
import com.mmisoft.loop_movies.data.model.remote.Movie
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieService: MovieService) :
    MovieRepository {
    override suspend fun getAllMovies(): List<Movie>? {
        val response = movieService.getAllMovies()
        if (response.isSuccessful) {
            return response.body()
        } else Log.e("Retrofit", "Failed to fetch Movies")
        return listOf()
    }

    override suspend fun getStaffPick(): List<Movie>? {
        val response = movieService.getStaffPick()
        if (response.isSuccessful) {
            return response.body()
        } else Log.e("Retrofit", "Failed to fetch Staff Pick")
        return listOf()
    }

}