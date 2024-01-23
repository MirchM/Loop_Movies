package com.mmisoft.loop_movies.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmisoft.loop_movies.data.model.remote.Cast
import com.mmisoft.loop_movies.data.model.remote.Director
import com.mmisoft.loop_movies.data.model.remote.Movie
import com.mmisoft.loop_movies.databinding.FragmentSearchBinding
import com.mmisoft.loop_movies.ui.dialog.FullscreenModalMovieBottomSheetDialog
import com.mmisoft.loop_movies.ui.recyclerview.RecyclerViewClickListener
import com.mmisoft.loop_movies.ui.recyclerview.adapter.RecyclerViewVerticalMovieAdapter
import com.mmisoft.loop_movies.ui.viewmodel.MovieViewModel
import com.mmisoft.loop_movies.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class SearchFragment : Fragment(), RecyclerViewClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    private val movieViewModel: MovieViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val verticalRecyclerView = binding.searchMoviesRecyclerview
        val layoutManager = LinearLayoutManager(this.context)
        verticalRecyclerView.layoutManager = layoutManager

        val verticalAdapter = RecyclerViewVerticalMovieAdapter(this)

        binding.searchSearchButton.setOnClickListener {
            if (binding.searchEditText.text.toString() == "") {
                movieViewModel.movies.value?.let { allMovies -> verticalAdapter.setMovies(allMovies) }
            } else {
                movieViewModel.searchMovies.observe(viewLifecycleOwner){
                    verticalAdapter.setMovies(it)
                }
                movieViewModel.searchMovie(binding.searchEditText.text.toString())
            }
        }



        movieViewModel.movies.observe(viewLifecycleOwner) {
            verticalAdapter.setMovies(it)
        }

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            verticalAdapter.setBookmarkedMovies(user.favouriteMovies)
        }

        verticalRecyclerView.adapter = verticalAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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