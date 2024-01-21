package com.mmisoft.loop_movies.ui.recyclerview.viewholder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.data.datamodel.RecyclerViewDataItem

class MovieImageViewViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val posterImageView: ImageView = itemView.findViewById(R.id.movie_item_image_view)

    override fun bind(item: RecyclerViewDataItem) {
        val posterUrl = item as RecyclerViewDataItem.ImageViewItem
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.error_round)
        Glide.with(posterImageView.context)
            .load(posterUrl.imageUrl)
            .apply(options).into(posterImageView)
    }
}
