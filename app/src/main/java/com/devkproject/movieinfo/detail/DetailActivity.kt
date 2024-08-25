package com.devkproject.movieinfo.detail

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.NetworkState
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.*
import com.devkproject.movieinfo.databinding.ActivityDetailBinding
import com.devkproject.movieinfo.databinding.DetailScrollBinding
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
import com.google.android.gms.ads.MobileAds
import com.google.android.material.appbar.AppBarLayout
import java.text.DecimalFormat
import kotlin.math.abs

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository
    private lateinit var creditsViewModel: CreditsViewModel
    private lateinit var creditsRepository: CreditsRepository
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var videosViewModel: VideosViewModel
    private lateinit var videosRepository: VideosRepository

    private var isFavorite: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.detail_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.run {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        }

        binding.detailAppbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                abs(verticalOffset) == appBarLayout!!.totalScrollRange -> {
                    binding.detailCollapsingToolbarLayout.title = intent.getStringExtra("title")
                    binding.detailCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
                }
                verticalOffset == 0 -> {
                    binding.detailCollapsingToolbarLayout.title = ""
                }
                else -> {
                    binding.detailCollapsingToolbarLayout.title = ""
                }
            }
        })

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adViewDetail.loadAd(adRequest)

        val movieId: Int = intent.getIntExtra("id", 1)
        val title = intent.getStringExtra("title")
        val releaseDate = intent.getStringExtra("release")
        val rating = intent.getDoubleExtra("rating", 1.0)
        val poster = intent.getStringExtra("poster")
        val movie = Favorite(movieId, title, releaseDate, rating, poster)

        val apiService: TMDBInterface = TMDBClient.getClient()

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        favoriteViewModel.allMovie.observe(this, Observer {
            if (it.contains(movie)) {
                binding.fabFavorite.setImageResource(R.drawable.ic_star_black_24dp)
                isFavorite = true
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_star_border_black_24dp)
                isFavorite = false
            }
        })

        binding.fabFavorite.setOnClickListener {
            if (isFavorite == false) {
                favoriteViewModel.insert(movie)
                binding.fabFavorite.setImageResource(R.drawable.ic_star_black_24dp)
            } else {
                favoriteViewModel.delete(movie)
                binding.fabFavorite.setImageResource(R.drawable.ic_star_border_black_24dp)
            }

            //throw IllegalStateException("Firebase Crashlytics Test")

        }

        detailRepository = DetailRepository(apiService)
        detailViewModel = ViewModelProvider(this, DetailViewModel.DetailViewModelFactory(detailRepository, movieId))
            .get(DetailViewModel::class.java)
        detailViewModel.detailMovie.observe(this, Observer {
            bindUI(it)
            setGenreRVAdapter(it.genres)
            setProductionRVAdapter(it.production_countries)
        })
        detailViewModel.networkState.observe(this, Observer {
            binding.detailProgressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.detailErrorText.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
//            binding.detailScrollItem.visibility = if(it == NetworkState.ERROR || it == NetworkState.LOADING) View.GONE else View.VISIBLE
            binding.detailCollapsingToolbarLayout.visibility = if(it == NetworkState.ERROR || it == NetworkState.LOADING) View.GONE else View.VISIBLE
        })

        creditsRepository = CreditsRepository(apiService)
        creditsViewModel = ViewModelProvider(this, CreditsViewModel.CreditsViewModelFactory(creditsRepository, movieId))
            .get(CreditsViewModel::class.java)
        creditsViewModel.creditsMovie.observe(this, Observer {
            setCastRVAdapter(it.cast)
            setCrewRVAdapter(it.crew)
        })

        videosRepository = VideosRepository(apiService)
        videosViewModel = ViewModelProvider(this, VideosViewModel.VideosViewModelFactory(videosRepository, movieId))
            .get(VideosViewModel::class.java)
        videosViewModel.videosMovie.observe(this, Observer {
            setVideosRVAdapter(it.results)
        })
    }

    private fun bindUI(it: TMDBDetail) {
        val decimalFormat = DecimalFormat("###,###")
        val decimalBudget = "$ " + decimalFormat.format(it.budget)
        val decimalRevenue = "$ " + decimalFormat.format(it.revenue)
        val runtime = it.runtime.toString() + " 분"
        val originalTitle = it.title + "\n(${it.original_title})"

        binding.detailScrollItem.detailMovieTitle.text = originalTitle
        binding.detailScrollItem.detailMovieTagline.text = it.tagline
        binding.detailScrollItem.detailMovieRelease.text = it.releaseDate
        binding.detailScrollItem.detailMovieVoteCount.text = it.vote_count.toString()
        binding.detailScrollItem.detailMovieRating.text = it.rating.toString()
        binding.detailScrollItem.detailMovieRuntime.text = runtime
        binding.detailScrollItem.detailMovieBudget.text = decimalBudget
        binding.detailScrollItem.detailMovieRevenue.text = decimalRevenue
        binding.detailScrollItem.detailMovieOverview.text = it.overview

        val moviePosterURL: String = POSTER_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.detailMoviePoster)
    }

    private fun setGenreRVAdapter(item: ArrayList<Genres>) {
        val genreRVAdapter = GenreRVAdapter(item)
        binding.detailScrollItem.genreRecyclerView.run {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = genreRVAdapter
        }
    }

    private fun setProductionRVAdapter(item: ArrayList<Production>) {
        val productionRVAdapter = ProductionRVAdapter(item)
        binding.detailScrollItem.productionRecyclerView.run {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = productionRVAdapter
        }
    }

    private fun setCastRVAdapter(item: ArrayList<TMDBCast>) {
        val creditsRVAdapter = CreditsRVAdapter(item, this)
        binding.detailScrollItem.creditsRecyclerView.run {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = creditsRVAdapter
        }
    }

    private fun setCrewRVAdapter(item: ArrayList<TMDBCrew>) {
        val crewRVAdapter = CrewRVAdapter(item, this)
        binding.detailScrollItem.crewRecyclerView.run {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = crewRVAdapter
        }
    }

    private fun setVideosRVAdapter(item: ArrayList<TMDBTrailers>) {
        val videosRVAdapter = VideosRVAdapter(item, this)
        binding.detailScrollItem.videosRecyclerView.run {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = videosRVAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.share_movie -> {
                shareMovie()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareMovie() {
        val title = intent.getStringExtra("title") + getString(R.string.next_line) +
                SHARE_URL + intent.getIntExtra("id", 1)
        val intent = ShareCompat.IntentBuilder.from(this)
            .setType(SHARE_TYPE)
            .setText(title)
            .createChooserIntent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        startActivity(intent)
    }
}
