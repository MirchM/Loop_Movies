package com.mmisoft.loop_movies.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.mmisoft.loop_movies.R

class MovieGenreCardView(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

    private var cardText: String = ""

    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.movie_genre_card, this)
    }

    fun setText(newCardText: String) {
        val text = findViewById<TextView>(R.id.movie_genre_card_text)
        text.text = newCardText
    }

    fun getCardText(): String = cardText

}
