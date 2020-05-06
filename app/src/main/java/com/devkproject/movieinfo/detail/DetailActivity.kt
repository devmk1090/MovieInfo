package com.devkproject.movieinfo.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.devkproject.movieinfo.db.Favorite
import com.devkproject.movieinfo.db.FavoriteViewModel
import com.devkproject.movieinfo.detail.credits.CreditsRVAdapter
import com.devkproject.movieinfo.detail.credits.CreditsRepository
import com.devkproject.movieinfo.detail.credits.CreditsViewModel
import com.devkproject.movieinfo.detail.credits.CrewRVAdapter
import com.devkproject.movieinfo.detail.videos.VideosRVAdapter
import com.devkproject.movieinfo.detail.videos.VideosRepository
import com.devkproject.movieinfo.detail.videos.VideosViewModel
import com.devkproject.movieinfo.model.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository
    private lateinit var creditsViewModel: CreditsViewModel
    private lateinit var creditsRepository: CreditsRepository
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var videosViewModel: VideosViewModel
    private lateinit var videosRepository: VideosRepository

    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        MobileAds.initialize(this) {}
        mAdView = this.findViewById(R.id.adView_detail)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val movieId: Int = intent.getIntExtra("id", 1)
        val title = intent.getStringExtra("title")
        val releaseDate = intent.getStringExtra("release")
        val rating = intent.getDoubleExtra("rating", 1.0)
        val poster = intent.getStringExtra("poster")
        val movie = Favorite(movieId, title, releaseDate, rating, poster)

        Log.d("DetailActivity", movieId.toString())

        val apiService: TMDBInterface = TMDBClient.getClient()
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        favoriteViewModel.allMovie.observe(this, Observer {
            if (it.contains(movie)) {
                favorite_btn.text = "찜 해제"
                favorite_btn.setTextColor(Color.GRAY)
            } else {
                favorite_btn.text = "찜 하기"
                favorite_btn.setTextColor(Color.BLACK)
            }
        })

        favorite_btn.setOnClickListener {
            if(favorite_btn.text == "찜 하기") {
                favoriteViewModel.insert(movie)
                favorite_btn.text = "찜 해제"
                favorite_btn.setTextColor(Color.GRAY)
            } else {
                favoriteViewModel.delete(movie)
                favorite_btn.text = "찜 하기"
                favorite_btn.setTextColor(Color.BLACK)
            }
        }

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

        videosRepository = VideosRepository(apiService)
        videosViewModel = getVideosViewModel(movieId)
        videosViewModel.videosMovie.observe(this, Observer {
            setVideosRVAdapter(it.results)
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

    private fun setVideosRVAdapter(item: ArrayList<TMDBTrailers>) {
        val videosRVAdapter = VideosRVAdapter(item, this)
        videos_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        videos_recyclerView.setHasFixedSize(true)
        videos_recyclerView.adapter = videosRVAdapter
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

    private fun getVideosViewModel(movieId: Int): VideosViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return VideosViewModel(videosRepository, movieId) as T
            }
        })[VideosViewModel::class.java]
    }
}
