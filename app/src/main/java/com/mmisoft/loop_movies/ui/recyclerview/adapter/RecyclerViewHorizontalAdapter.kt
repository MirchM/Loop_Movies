package com.mmisoft.loop_movies.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.data.datamodel.RecyclerViewDataItem
import com.mmisoft.loop_movies.ui.recyclerview.viewholder.BaseViewHolder
import com.mmisoft.loop_movies.ui.recyclerview.viewholder.MovieImageViewViewHolder
import com.mmisoft.loop_movies.ui.recyclerview.viewholder.RecyclerViewButtonViewHolder
import com.mmisoft.loop_movies.ui.recyclerview.ViewTypeResolver
import java.lang.IllegalArgumentException

class RecyclerViewHorizontalAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val recyclerViewItems: MutableList<RecyclerViewDataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutRes: Int = when (viewType) {
            ViewTypeResolver.TYPE_POSTER_VIEW -> R.layout.movie_item_horizontal
            ViewTypeResolver.TYPE_BUTTON -> R.layout.movie_item_button_horizontal
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }

        return when (viewType) {
            ViewTypeResolver.TYPE_POSTER_VIEW -> MovieImageViewViewHolder(
                LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
            )

            ViewTypeResolver.TYPE_BUTTON -> RecyclerViewButtonViewHolder(
                LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(recyclerViewItems[position])
    }

    override fun getItemCount(): Int {
        return recyclerViewItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (recyclerViewItems[position]) {
            is RecyclerViewDataItem.ImageViewItem -> ViewTypeResolver.TYPE_POSTER_VIEW
            is RecyclerViewDataItem.ButtonItem -> ViewTypeResolver.TYPE_BUTTON
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<RecyclerViewDataItem>) {
        recyclerViewItems.clear()
        recyclerViewItems.addAll(data)
        notifyDataSetChanged()
    }
}

