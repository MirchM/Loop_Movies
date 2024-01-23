package com.mmisoft.loop_movies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmisoft.loop_movies.data.model.remote.Movie
import com.mmisoft.loop_movies.repository.network.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _searchMovies = MutableLiveData<List<Movie>>()
    val searchMovies: LiveData<List<Movie>> = _searchMovies

    private val _staffPicks = MutableLiveData<List<Movie>>()
    val staffPicks: LiveData<List<Movie>> = _staffPicks

    private val _bookmarkedMovies = MutableLiveData<List<Movie>>()
    val bookmarkedMovies: LiveData<List<Movie>> = _bookmarkedMovies

    fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _movies.postValue(movieRepository.getAllMovies())
        }
        viewModelScope.launch(Dispatchers.IO) {
            _staffPicks.postValue(movieRepository.getStaffPick())
        }
    }

    fun searchMovie(searchTerm: String) {
        viewModelScope.launch {
            _searchMovies.postValue(_movies.value?.filter {
                it.title.lowercase().contains(searchTerm.lowercase())
            })
        }
    }

    fun searchBookmarkedMovies(movieIdList: List<Int>) {
        viewModelScope.launch {
            val bookmarkedMovies = mutableListOf<Movie>()
            _movies.value?.let { allMovies ->
                for (movie in allMovies) {
                    if (movieIdList.contains(movie.id)) {
                        bookmarkedMovies.add(movie)
                    }
                }

            }
            _bookmarkedMovies.postValue(bookmarkedMovies)
        }
    }

}