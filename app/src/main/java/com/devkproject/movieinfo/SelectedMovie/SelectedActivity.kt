package com.devkproject.movieinfo.SelectedMovie

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
import kotlinx.android.synthetic.main.activity_selected.*

class SelectedActivity : AppCompatActivity() {

    private lateinit var viewModel: SelectedViewModel
    private lateinit var selectedMovieRepository: SelectedMovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()
        selectedMovieRepository = SelectedMovieRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.selectedMovie.observe(this, Observer {
            bindUI(it)
        })
    }

    private fun bindUI(it: TMDBDetail) {
        selected_movie_title.text = it.title
        selected_movie_tagline.text = it.tagline
        selected_movie_release.text = it.releaseDate
        selected_movie_rating.text = it.rating.toString()
        selected_movie_overview.text = it.overview

        val moviePosterURL: String = POSTER_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(selected_poster_iv)
    }

    private fun getViewModel(movieId: Int): SelectedViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SelectedViewModel(selectedMovieRepository, movieId) as T
            }
        })[SelectedViewModel::class.java]
    }
}
