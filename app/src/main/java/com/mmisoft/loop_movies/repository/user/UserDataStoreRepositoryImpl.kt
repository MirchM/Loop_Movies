package com.mmisoft.loop_movies.repository.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val PREFERENCES_NAME = "user_preferences"
private const val USER_NAME_KEY = "user_name"
private const val USER_FAVOURITE_MOVIES = "user_favourites"

//private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

private val Context.dataStore by preferencesDataStore(
    name = PREFERENCES_NAME
)

class UserDataStoreRepositoryImpl @Inject constructor(private val context: Context) :
    UserRepository {


    override suspend fun getUserName(): String? {
        return try {
            val userNameKey = stringPreferencesKey(USER_NAME_KEY)
            val preferences = context.dataStore.data.first()
            return preferences[userNameKey]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getUserFavouriteMovies(): String? {
        return try {
            val favouriteMoviesKey = stringPreferencesKey(USER_FAVOURITE_MOVIES)
            val preferences = context.dataStore.data.first()
            return preferences[favouriteMoviesKey]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun saveUserName(userName: String) {
        val userNameKey = stringPreferencesKey(USER_NAME_KEY)
        context.dataStore.edit { preferences ->
            preferences[userNameKey] = userName
        }
    }

    override suspend fun saveFavouriteMovies(serializedList: String) {
        val favouriteMoviesKey = stringPreferencesKey(USER_FAVOURITE_MOVIES)
        context.dataStore.edit { preferences ->
            preferences[favouriteMoviesKey] = serializedList
        }
    }

    override suspend fun fetchUser(): Pair<String?, String?> {
        return Pair(getUserName(), getUserFavouriteMovies())
    }


}