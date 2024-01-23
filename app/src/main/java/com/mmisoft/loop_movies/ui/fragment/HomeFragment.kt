package com.mmisoft.loop_movies.ui.fragment

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.data.model.remote.AuthenticationState
import com.mmisoft.loop_movies.databinding.FragmentHomeBinding
import com.mmisoft.loop_movies.ui.dialog.FullscreenModalMovieBottomSheetDialog
import com.mmisoft.loop_movies.ui.recyclerview.RecyclerViewClickListener
import com.mmisoft.loop_movies.ui.recyclerview.adapter.RecyclerViewHorizontalAdapter
import com.mmisoft.loop_movies.ui.recyclerview.adapter.RecyclerViewVerticalMovieAdapter
import com.mmisoft.loop_movies.ui.viewmodel.MovieViewModel
import com.mmisoft.loop_movies.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment(), RecyclerViewClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel: MovieViewModel by activityViewModels()

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //movieViewModel.fetchMovies()
        // User Data

        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            binding.userName.text = user.name
        })
        setStoredProfileImage()

        // Firebase Login
        userViewModel.checkIfUserLoggedIn()
        if (userViewModel.authenticationState.value != AuthenticationState.Authenticated) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        binding.searchAllMoviesButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        // RecyclerView
        val recyclerView = binding.horizontalMovieImageRecyclerview
        val horizontalLayoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        val horizontalRecyclerViewAdapter = RecyclerViewHorizontalAdapter(this)
        horizontalRecyclerViewAdapter.setData(listOf())
        recyclerView.adapter = horizontalRecyclerViewAdapter


        val verticalRecyclerView = binding.staffPicksRecyclerview
        val layoutManager = LinearLayoutManager(this.context)
        verticalRecyclerView.layoutManager = layoutManager
        val verticalAdapter = RecyclerViewVerticalMovieAdapter(this)
        verticalRecyclerView.adapter = verticalAdapter
        verticalAdapter.setMovies(listOf())

        movieViewModel.staffPicks.observe(viewLifecycleOwner) {
            verticalAdapter.setMovies(it)
        }
        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            verticalAdapter.setBookmarkedMovies(user.favouriteMovies)
            movieViewModel.searchBookmarkedMovies(user.favouriteMovies)

        })
        movieViewModel.bookmarkedMovies.observe(viewLifecycleOwner) {
            horizontalRecyclerViewAdapter.setData(it)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setStoredProfileImage() {
        val imageFile = File(
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "profilePicture.jpg"
        )
        if (imageFile.exists()) {
            // Image exists, proceed to set it to the ImageView
            Log.d("PROFILE_PICTURE", "EXISTS")
            binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(imageFile.path))
        } else {
            // Image doesn't exist yet, don't set it to the ImageView
            Log.d("PROFILE_PICTURE", "DOESN'T EXIST")
        }
    }

    override fun onMovieClick(movieId: Int) {
        movieViewModel.movies.value?.find { it.id == movieId }?.let { movie ->
            userViewModel.user.value?.favouriteMovies?.let { favouriteMovies ->
                val fullScreenMovieDetail = FullscreenModalMovieBottomSheetDialog(
                    movie,
                    favouriteMovies.contains(movieId),
                ) { userViewModel.saveUserFavouriteMovies(movieId) }
                parentFragmentManager.let {
                    fullScreenMovieDetail.show(
                        it,
                        "fullScreenMovieDetailDialog"
                    )
                }
            }
        }
    }

    override fun onBookmarkClick(movieId: Int) {
        userViewModel.saveUserFavouriteMovies(movieId)
    }
}