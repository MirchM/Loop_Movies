package com.mmisoft.loop_movies.ui.recyclerview

import com.mmisoft.loop_movies.data.model.remote.Movie
import java.lang.IllegalArgumentException

class ViewTypeResolver {

    fun getType(item: Any): Int {
        return when (item) {
            is Movie -> TYPE_POSTER_VIEW
            is String -> TYPE_BUTTON
            else -> throw IllegalArgumentException("Invalid item type: ${item.javaClass.name}")
        }
    }

    companion object {
        const val TYPE_POSTER_VIEW = 1
        const val TYPE_BUTTON = 2
    }
}