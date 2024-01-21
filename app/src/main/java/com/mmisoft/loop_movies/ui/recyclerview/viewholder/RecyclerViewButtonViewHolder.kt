package com.mmisoft.loop_movies.ui.recyclerview.viewholder

import android.view.View
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.data.datamodel.RecyclerViewDataItem

class RecyclerViewButtonViewHolder(itemView: View) :
    BaseViewHolder(itemView) {

    private val button: View = itemView.findViewById(R.id.see_all_movies_button)

    override fun bind(item: RecyclerViewDataItem) {
        val seeAllButton = item as RecyclerViewDataItem.ButtonItem
        button.setOnClickListener(seeAllButton.onClickListener)
    }
}