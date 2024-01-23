package com.mmisoft.loop_movies.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.data.model.remote.Movie
import com.mmisoft.loop_movies.databinding.MovieItemVerticalBinding
import com.mmisoft.loop_movies.ui.recyclerview.RecyclerViewClickListener




class RecyclerViewVerticalMovieAdapter(private val onRecyclerViewClickListener: RecyclerViewClickListener) :
    RecyclerView.Adapter<RecyclerViewVerticalMovieAdapter.ViewHolder>() {

    private val movies: MutableList<Movie> = mutableListOf()
    private val bookmarkedMovies: MutableList<Int> = mutableListOf()

    inner class ViewHolder(val binding: MovieItemVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MovieItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMovie = movies[position]
        // Image - Poster
        val movieItemImage = holder.binding.movieItemImageView
        val posterUrl = currentMovie.posterUrl
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.error_round)
        Glide.with(movieItemImage.context)
            .load(posterUrl)
            .apply(options).into(movieItemImage)

        movieItemImage.setOnClickListener{
            onRecyclerViewClickListener.onMovieClick(currentMovie.id)
        }

        // Text
        holder.binding.movieItemMovieTitle.text = currentMovie.title
        holder.binding.movieItemReleaseYear.text = currentMovie.releaseDate.take(4)

        // Rating
        currentMovie.rating.let { holder.binding.movieItemRating.setRating(it, small = true) }

        // Bookmark
        if(currentMovie.id in bookmarkedMovies){
            holder.binding.movieItemBookmarkButton.setImageResource(R.drawable.bookmark_filled)
        }else{
            holder.binding.movieItemBookmarkButton.setImageResource(R.drawable.bookmark_empty)
        }
        holder.binding.movieItemBookmarkButton.setOnClickListener{onRecyclerViewClickListener.onBookmarkClick(currentMovie.id)}
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(newMovieList: List<Movie>) {
        movies.clear()
        movies.addAll(newMovieList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBookmarkedMovies(newBookmarkedMovies: List<Int>){
        bookmarkedMovies.clear()
        bookmarkedMovies.addAll(newBookmarkedMovies)
        notifyDataSetChanged()
    }

}