package com.mmisoft.loop_movies.ui.recyclerview

import android.view.View
import com.mmisoft.loop_movies.data.model.remote.Movie

sealed class RecyclerViewDataItem {

    class ImageViewItem(val movie: Movie) : RecyclerViewDataItem()
    class ButtonItem(val onClickListener: View.OnClickListener?) : RecyclerViewDataItem()
}
