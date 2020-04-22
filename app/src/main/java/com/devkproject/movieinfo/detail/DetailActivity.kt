package com.devkproject.movieinfo.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
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
import kotlinx.android.synthetic.main.fragment_credits.*
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var selectedMovieRepository: DetailRepository
    private lateinit var creditsViewModel: CreditsViewModel
    private lateinit var creditsRepository: CreditsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()
        selectedMovieRepository = DetailRepository(apiService)
        creditsRepository = CreditsRepository(apiService)
        creditsViewModel = getViewModel2(movieId)
        viewModel = getViewModel(movieId)
        viewModel.selectedMovie.observe(this, Observer {
            bindUI(it)
            setGenreRVAdapter(it.genres)
            setProductionRVAdapter(it.production_countries)
        })
        creditsViewModel.creditsMovie.observe(this, Observer {
            setCastRVAdapter(it.cast)
            setCrewRVAdapter(it.crew)
        })

//        val pagerAdapter= PagerAdapter(supportFragmentManager)
//        val pager = findViewById<ViewPager>(R.id.view_pager)
//        pager.adapter = pagerAdapter
//        tab_layout.setupWithViewPager(pager)
//        tab_layout.setTabTextColors(Color.GRAY, Color.BLACK)

    }

    private fun bindUI(it: TMDBDetail) {
        val decimalFormat = DecimalFormat("###,###")
        val decimalBudget = decimalFormat.format(it.budget) + " $"
        val decimalRevenue = decimalFormat.format(it.revenue) + " $"
        val runtime = it.runtime.toString() + " 분"
        val originalTitle = it.title + "\n(${it.original_title})"
        selected_movie_title.text = originalTitle
        selected_movie_tagline.text = it.tagline

        info_release.text = it.releaseDate
        info_rating.text = it.rating.toString()
        info_vote_count.text = it.vote_count.toString()
        info_budget.text = decimalBudget
        info_revenue.text = decimalRevenue
        info_runtime.text = runtime
        info_overview.text = it.overview

        val moviePosterURL: String = POSTER_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(selected_poster_iv)
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

    private fun getViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(selectedMovieRepository, movieId) as T
            }
        })[DetailViewModel::class.java]
    }
    private fun getViewModel2(movieId: Int): CreditsViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CreditsViewModel(
                    creditsRepository,
                    movieId
                ) as T
            }
        })[CreditsViewModel::class.java]
    }
}
