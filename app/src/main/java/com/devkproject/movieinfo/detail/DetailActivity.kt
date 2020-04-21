package com.devkproject.movieinfo.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import kotlinx.android.synthetic.main.activity_detail.*

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

        val pagerAdapter= PagerAdapter(supportFragmentManager)
        val pager = findViewById<ViewPager>(R.id.view_pager)
        pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(pager)
    }

    private fun bindUI(it: TMDBDetail) {
        val originalTitle = it.title + "\n(${it.original_title})"
        selected_movie_title.text = originalTitle
        selected_movie_tagline.text = it.tagline

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
