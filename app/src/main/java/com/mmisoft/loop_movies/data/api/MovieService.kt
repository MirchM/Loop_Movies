package com.mmisoft.loop_movies.data.api

import com.mmisoft.loop_movies.data.model.remote.Movie
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {
    @GET("movies.json")
    suspend fun getAllMovies(): Response<List<Movie>>

    @GET("staff_picks.json")
    suspend fun getStaffPick(): Response<List<Movie>>
}