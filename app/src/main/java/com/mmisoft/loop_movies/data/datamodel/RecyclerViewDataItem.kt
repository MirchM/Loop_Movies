package com.mmisoft.loop_movies.data.datamodel

import android.view.View

sealed class RecyclerViewDataItem {

    class ImageViewItem(val imageUrl: String) : RecyclerViewDataItem()
    class ButtonItem(val onClickListener: View.OnClickListener?) : RecyclerViewDataItem()
}
