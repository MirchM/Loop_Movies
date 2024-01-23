package com.mmisoft.loop_movies.ui.recyclerview.viewholder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.ui.recyclerview.RecyclerViewClickListener
import com.mmisoft.loop_movies.ui.recyclerview.RecyclerViewDataItem


class MovieImageViewViewHolder(itemView: View, private val onRecyclerViewClickListener: RecyclerViewClickListener) : BaseViewHolder(itemView, onRecyclerViewClickListener) {

    private val posterImageView: ImageView = itemView.findViewById(R.id.movie_item_image_view)

    private val listener: RecyclerViewClickListener? = null

    override fun bind(item: RecyclerViewDataItem) {
        val dataItem = item as RecyclerViewDataItem.ImageViewItem
        val movie = dataItem.movie
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.error_round)
        Glide.with(posterImageView.context)
            .load(movie.posterUrl)
            .apply(options).into(posterImageView)

        posterImageView.setOnClickListener{
            onRecyclerViewClickListener.onMovieClick(movieId = movie.id)
        }
    }
}
