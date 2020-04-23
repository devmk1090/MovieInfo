package com.devkproject.movieinfo.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.detail.credits.CreditsRVAdapter
import com.devkproject.movieinfo.detail.credits.CreditsRepository
import com.devkproject.movieinfo.detail.credits.CreditsViewModel
import com.devkproject.movieinfo.detail.credits.CrewRVAdapter
import com.devkproject.movieinfo.model.*
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository
    private lateinit var creditsViewModel: CreditsViewModel
    private lateinit var creditsRepository: CreditsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()

        detailRepository = DetailRepository(apiService)
        detailViewModel = getDetailViewModel(movieId)
        detailViewModel.detailMovie.observe(this, Observer {
            bindUI(it)
            setGenreRVAdapter(it.genres)
            setProductionRVAdapter(it.production_countries)
        })
        detailViewModel.networkState.observe(this, Observer {
            detail_progress_bar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            detail_error_text.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

        creditsRepository = CreditsRepository(apiService)
        creditsViewModel = getCreditsViewModel(movieId)
        creditsViewModel.creditsMovie.observe(this, Observer {
            setCastRVAdapter(it.cast)
            setCrewRVAdapter(it.crew)
        })
    }

    private fun bindUI(it: TMDBDetail) {
        val decimalFormat = DecimalFormat("###,###")
        val decimalBudget = decimalFormat.format(it.budget) + " $"
        val decimalRevenue = decimalFormat.format(it.revenue) + " $"
        val runtime = it.runtime.toString() + " 분"
        val originalTitle = it.title + "\n(${it.original_title})"

        detail_movie_title.text = originalTitle
        detail_movie_tagline.text = it.tagline
        detail_movie_release.text = it.releaseDate
        detail_movie_voteCount.text = it.vote_count.toString()
        detail_movie_rating.text = it.rating.toString()
        detail_movie_runtime.text = runtime
        detail_movie_budget.text = decimalBudget
        detail_movie_revenue.text = decimalRevenue
        detail_movie_overview.text = it.overview

        val moviePosterURL: String = POSTER_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(detail_movie_poster)
    }

    private fun setGenreRVAdapter(item: ArrayList<Genres>) {
        val genreRVAdapter = GenreRVAdapter(item)
        genre_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        genre_recyclerView.setHasFixedSize(true)
        genre_recyclerView.adapter = genreRVAdapter
    }

    private fun setProductionRVAdapter(item: ArrayList<Production>) {
        val productionRVAdapter = ProductionRVAdapter(item)
        production_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        production_recyclerView.setHasFixedSize(true)
        production_recyclerView.adapter = productionRVAdapter
    }

    private fun setCastRVAdapter(item: ArrayList<TMDBCast>) {
        val creditsRVAdapter = CreditsRVAdapter(item)
        credits_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        credits_recyclerView.setHasFixedSize(true)
        credits_recyclerView.adapter = creditsRVAdapter
    }

    private fun setCrewRVAdapter(item: ArrayList<TMDBCrew>) {
        val crewRVAdapter = CrewRVAdapter(item)
        crew_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        crew_recyclerView.setHasFixedSize(true)
        crew_recyclerView.adapter = crewRVAdapter
    }

    private fun getDetailViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(detailRepository, movieId) as T
            }
        })[DetailViewModel::class.java]
    }
    private fun getCreditsViewModel(movieId: Int): CreditsViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CreditsViewModel(creditsRepository, movieId) as T
            }
        })[CreditsViewModel::class.java]
    }
}
