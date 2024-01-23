package com.mmisoft.loop_movies.repository.user

import com.mmisoft.loop_movies.data.model.local.User

interface UserRepository {
    suspend fun getUserName(): String?

    suspend fun getUserFavouriteMovies(): String?
    suspend fun saveUserName(userName: String)

    suspend fun saveFavouriteMovies(serializedList: String)
}
