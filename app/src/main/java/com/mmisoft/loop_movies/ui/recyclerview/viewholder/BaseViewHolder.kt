package com.mmisoft.loop_movies.ui.recyclerview.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mmisoft.loop_movies.data.datamodel.RecyclerViewDataItem

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    abstract fun bind(item: RecyclerViewDataItem)
}
