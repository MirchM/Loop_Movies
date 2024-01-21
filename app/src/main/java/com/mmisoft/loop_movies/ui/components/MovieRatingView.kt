package com.mmisoft.loop_movies.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.mmisoft.loop_movies.R
import kotlin.math.roundToInt

class MovieRatingView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var rating : Int = 0
    private var ratingStars = arrayListOf<ImageView>()


    // Secondary constructor to set the rating
    constructor(context: Context, attrs: AttributeSet?, rating: Int) : this(context, attrs) {
        initView()
        this.rating = rating
    }

    init {
        initView()
    }



    private fun initView() {

        View.inflate(context, R.layout.rating_view, this)

        val star1 = findViewById<ImageView>(R.id.rating_star_0)
        val star2 = findViewById<ImageView>(R.id.rating_star_1)
        val star3 = findViewById<ImageView>(R.id.rating_star_2)
        val star4 = findViewById<ImageView>(R.id.rating_star_3)
        val star5 = findViewById<ImageView>(R.id.rating_star_4)

        ratingStars.add(star1)
        ratingStars.add(star2)
        ratingStars.add(star3)
        ratingStars.add(star4)
        ratingStars.add(star5)
    }

    fun setRating(rating: Double) {
        this.rating = rating.roundToInt()

        for (i in 0 until this.rating) {
            val star = ratingStars[i]
            star.setImageResource(R.drawable.rating_star_filled)
        }

        for (i in this.rating until 5) {
            val star = ratingStars[i]
            star.setImageResource(R.drawable.rating_star_empty)
        }
    }

    private fun getStarById(starNumber: Int): ImageView {
        return findViewById(resources.getIdentifier("rating_star_$starNumber", "id", context.packageName))
    }
}