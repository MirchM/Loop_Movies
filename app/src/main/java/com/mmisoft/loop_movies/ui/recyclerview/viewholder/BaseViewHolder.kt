package com.mmisoft.loop_movies.ui.recyclerview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mmisoft.loop_movies.ui.recyclerview.RecyclerViewClickListener
import com.mmisoft.loop_movies.ui.recyclerview.RecyclerViewDataItem

abstract class BaseViewHolder(itemView: View, onRecyclerViewClickListener: RecyclerViewClickListener) : RecyclerView.ViewHolder(itemView){
    abstract fun bind(item: RecyclerViewDataItem)
}
