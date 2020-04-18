package com.devkproject.movieinfo.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var selectedMovieRepository: DetailRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()
        selectedMovieRepository = DetailRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.selectedMovie.observe(this, Observer {
            bindUI(it)
        })
    }

    private fun bindUI(it: TMDBDetail) {
        val decimalFormat = DecimalFormat("###,###")
        val decimalBudget = decimalFormat.format(it.budget) + " $"
        val decimalRevenue = decimalFormat.format(it.revenue) + " $"
        val originalTitle = it.title + "\n(${it.original_title})"
        val runtime = it.runtime.toString() + " 분"
        selected_movie_title.text = originalTitle
        selected_movie_tagline.text = it.tagline
        selected_movie_release.text = it.releaseDate
        selected_movie_rating.text = it.rating.toString()
        selected_movie_vote_count.text = it.vote_count.toString()
        selected_movie_budget.text = decimalBudget
        selected_movie_revenue.text = decimalRevenue
        selected_movie_overview.text = it.overview
        selected_movie_runtime.text = runtime


        val moviePosterURL: String = POSTER_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(selected_poster_iv)
    }

    private fun getViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(selectedMovieRepository, movieId) as T
            }
        })[DetailViewModel::class.java]
    }
}
