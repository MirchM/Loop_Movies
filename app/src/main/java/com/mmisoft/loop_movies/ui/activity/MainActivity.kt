package com.mmisoft.loop_movies.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.ui.viewmodel.MovieViewModel
import com.mmisoft.loop_movies.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val movieViewModel: MovieViewModel by viewModels()

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController


        movieViewModel.fetchMovies()
        //userViewModel.getUserFavouriteMovies()
        //userViewModel.user.value?.favouriteMovies?.let { movieViewModel.searchBookmarkedMovies(it) }
    }

    override fun onStart() {
        super.onStart()
        userViewModel.checkIfUserLoggedIn()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
