package com.mmisoft.loop_movies.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.mmisoft.loop_movies.R

class MovieFactCard(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {
    private var cardTitle: String = ""
    private var cardBody: String = ""

    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.movie_facts_card, this)
    }

    fun setText(newFactTitle: String, newFactBody: String) {
        val factTitle = findViewById<TextView>(R.id.movie_fact_card_title)
        factTitle.text = newFactTitle

        val factBody = findViewById<TextView>(R.id.movie_fact_card_body)
        factBody.text = newFactBody
    }

    fun getCardText(): List<String> = listOf(cardTitle, cardBody)
}
