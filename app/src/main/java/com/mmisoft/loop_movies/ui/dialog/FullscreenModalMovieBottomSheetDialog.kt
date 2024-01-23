package com.mmisoft.loop_movies.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmisoft.loop_movies.R
import com.mmisoft.loop_movies.data.model.remote.Movie
import com.mmisoft.loop_movies.databinding.MovieDetailBottomSheetFullscreenBinding
import com.mmisoft.loop_movies.ui.components.MovieGenreCardView
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale


class FullscreenModalMovieBottomSheetDialog(val movie: Movie, private var isBookmarked: Boolean, val onBookmarkClick: ()->Unit) : BottomSheetDialogFragment() {
    private lateinit var binding: MovieDetailBottomSheetFullscreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieDetailBottomSheetFullscreenBinding.inflate(inflater, container, false)

        // Close Button
        val toolbar = binding.toolbar
        toolbar.setOnClickListener {
            dismiss()
        }

        setBookmarkResource()

        binding.bookmark.setOnClickListener{
            onBookmarkClick()
            isBookmarked = !isBookmarked
            setBookmarkResource()
        }

        // Movie Image
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.error_round)
        context?.let {
            Glide.with(it)
                .load(movie.posterUrl)
                .apply(options).into(binding.movieItemImageView.movieItemImageView)
        }

        // Movie Rating Component
        val movieRating = binding.movieRating
        movie.rating.let { movieRating.setRating(it) }

        // Movie Release Date and Duration
        binding.movieReleaseAndDuration.text = getString(
            R.string.movie_detail_screen_release_and_duration,
            movie.releaseDate.replace('-', '.'),
            movie.runtime.div(60).toString(),
            movie.runtime.rem(60).toString()
        )


        // Movie Title
        binding.movieTitle.text = movie.title

        // Movie Release Year
        binding.movieReleaseYear.text =
            getString(R.string.movie_detail_screen_release_year, movie.releaseDate.take(4))


        // Movie Genre Cards
        val genreCards = binding.movieGenreCardHolder
        movie.genres.let { movieGenres ->
            for (movieGenre in movieGenres) {
                val newCard = this.context?.let { MovieGenreCardView(it, null) }
                newCard?.setText(movieGenre)
                newCard?.cardElevation = 0f
                genreCards.addView(newCard)
            }
        }

        // TODO Movie overview
        val overviewBody = binding.overviewBody
        overviewBody.text = movie.overview

        // Movie Key Facts
        val budgetFact = binding.movieBudgetCard
        movie.budget.let { budgetFact.setText("Budget", "${formatNumber(it)}$") }
        budgetFact.cardElevation = 0f

        val revenueFact = binding.movieRevenueCard
        movie.revenue.let { revenueFact.setText("Revenue", "${formatNumber(it)}$") }
        revenueFact.cardElevation = 0f

        val languageFact = binding.movieOriginalLanguageCard
        movie.language.let {
            languageFact.setText(
                "Original Language",
                languageCodeToLanguageName(it)
            )
        }
        languageFact.cardElevation = 0f

        val ratingFact = binding.movieRatingCard
        movie.rating.let {
            ratingFact.setText(
                "Rating",
                "${roundUpRating(it)} (${movie.reviews})"
            )
        }
        ratingFact.cardElevation = 0f
        //val movieFactHolder = binding.movieFactHolder1

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet: FrameLayout =
            dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!

        // Height of the view
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        // Behavior of the bottom sheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.apply {
            peekHeight = resources.displayMetrics.heightPixels // Pop-up height
            state = BottomSheetBehavior.STATE_EXPANDED // Expanded state

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    private fun setBookmarkResource(){
        if(isBookmarked){
            binding.bookmark.setImageResource(R.drawable.bookmark_filled)
        }else{
            binding.bookmark.setImageResource(R.drawable.bookmark_empty)
        }
    }

    private fun formatNumber(numberToFormat: Long): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(numberToFormat).replace(",", ".")
    }

    private fun languageCodeToLanguageName(languageCode: String): String {
        val locale = Locale(languageCode)
        return locale.getDisplayLanguage(locale)
    }

    private fun roundUpRating(rating: Double): Double =
        BigDecimal(rating).setScale(2, RoundingMode.HALF_UP).toDouble()

}
