package com.devkproject.movieinfo

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.devkproject.movieinfo.toprated.TopRatedRepository
import com.devkproject.movieinfo.toprated.TopRatedViewModel
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.databinding.MainDrawerBinding
import com.devkproject.movieinfo.db.FavoriteRVAdapter
import com.devkproject.movieinfo.db.FavoriteViewModel
import com.devkproject.movieinfo.genre.GenreViewModel
import com.devkproject.movieinfo.now_playing.NowPlayingViewModel
import com.devkproject.movieinfo.popular.PopularViewModel
import com.devkproject.movieinfo.popular.PopularRepository
import com.devkproject.movieinfo.search.SearchViewModel
import com.devkproject.movieinfo.upcoming.UpcomingRepository
import com.devkproject.movieinfo.upcoming.UpcomingViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: MainDrawerBinding

    private val apiService: TMDBInterface = TMDBClient.getClient()
    private var movieAdapter = PagedListRVAdapter(this)

    private lateinit var popularViewModel: PopularViewModel
    private lateinit var popularRepository: PopularRepository

    private lateinit var topRatedViewModel: TopRatedViewModel
    private lateinit var topRatedRepository: TopRatedRepository

    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var upcomingRepository: UpcomingRepository

    private lateinit var searchViewModel: SearchViewModel
    private var searchView: SearchView? = null

    private lateinit var genreViewModel: GenreViewModel

    private lateinit var nowPlayingViewModel: NowPlayingViewModel

    private lateinit var favoriteViewModel: FavoriteViewModel

    private var firstTime : Long = 0
    private var secondTime : Long = 0

    private lateinit var mAdView: AdView

    companion object {
        const val GENRE_POPULAR = "popularity.desc"
        const val GENRE_RATED = "vote_average.desc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        mAdView = this.findViewById(R.id.adView_main)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        binding.mainDrawerNavigationView.setNavigationItemSelectedListener(this)
        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.run {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true) //드로어를 꺼낼 홈 버튼 활성화
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp) //홈버튼 이미지 변경
        }
        nowPlaying()
    }

    private fun movieSetting() {
        movieAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = movieAdapter.getItemViewType(position)
                return if(viewType == movieAdapter.MOVIE_TYPE) 1 else 3
            }
        }
        binding.mainContentItem.movieRecyclerView.run {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun nowPlaying() {
        supportActionBar!!.title = "현재 상영작"
        nowPlayingViewModel = ViewModelProvider(this, NowPlayingViewModel.NowPlayingViewModelFactory(apiService))
            .get(NowPlayingViewModel::class.java)
        movieSetting()
        nowPlayingViewModel.nowPlayingView().observe(this, Observer {
            movieAdapter.submitList(it)
        })
        nowPlayingViewModel.nowPlayingNetworkState().observe(this, Observer {
            binding.mainContentItem.movieProgressBar.visibility = if(nowPlayingViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.mainContentItem.movieErrorText.visibility = if(nowPlayingViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if(!nowPlayingViewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.popular_movie -> {
                supportActionBar!!.title = "인기 영화"

                popularRepository = PopularRepository(apiService)
                popularViewModel = ViewModelProvider(this, PopularViewModel.PopularViewModelFactory(popularRepository))
                    .get(PopularViewModel::class.java)

                movieSetting()
                popularViewModel.popularPagedList.observe(this, Observer {
                    movieAdapter.submitList(it)
                })
                popularViewModel.networkState.observe(this, Observer {
                    binding.mainContentItem.movieProgressBar.visibility = if(popularViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                    binding.mainContentItem.movieErrorText.visibility = if(popularViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
                    if(!popularViewModel.listIsEmpty()) {
                        movieAdapter.setNetworkState(it)
                    }
                })
            }
            R.id.topRated_movie -> {
                supportActionBar!!.title = "높은 평점순"

                topRatedRepository = TopRatedRepository(apiService)
                topRatedViewModel = ViewModelProvider(this, TopRatedViewModel.TopRatedViewModelFactory(topRatedRepository))
                    .get(TopRatedViewModel::class.java)
                movieSetting()
                topRatedViewModel.topRatedPagedList.observe(this, Observer {
                    movieAdapter.submitList(it)
                })
                topRatedViewModel.networkState.observe(this, Observer {
                    binding.mainContentItem.movieProgressBar.visibility = if(topRatedViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                    binding.mainContentItem.movieErrorText.visibility = if(topRatedViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
                    if(!topRatedViewModel.listIsEmpty()) {
                        movieAdapter.setNetworkState(it)
                    }
                })
            }
            R.id.nowPlaying_movie -> {
                nowPlaying()
            }
            R.id.upcoming_movie -> {
                supportActionBar!!.title = "개봉 예정"

                upcomingRepository = UpcomingRepository(apiService)
                upcomingViewModel = ViewModelProvider(this, UpcomingViewModel.UpcomingViewModelFactory(upcomingRepository))
                    .get(UpcomingViewModel::class.java)
                movieSetting()
                upcomingViewModel.upcomingPagedList.observe(this, Observer {
                    movieAdapter.submitList(it)
                })
                upcomingViewModel.networkState.observe(this, Observer {
                    binding.mainContentItem.movieProgressBar.visibility = if(upcomingViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                    binding.mainContentItem.movieErrorText.visibility = if(upcomingViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
                    if(!upcomingViewModel.listIsEmpty()) {
                        movieAdapter.setNetworkState(it)
                    }
                })
            }
            R.id.genre_popular_movie -> {
                val items = resources.getStringArray(R.array.genre_item)
                AlertDialog.Builder(this, 2)
                    .setTitle("장르 선택")
                    .setItems(R.array.genre_item) { dialog, which ->
                        when(which) {
                            0 -> genreId("28", items[which], GENRE_POPULAR)
                            1 -> genreId("12", items[which], GENRE_POPULAR)
                            2 -> genreId("16", items[which], GENRE_POPULAR)
                            3 -> genreId("35", items[which], GENRE_POPULAR)
                            4 -> genreId("10749", items[which], GENRE_POPULAR)
                            5 -> genreId("99", items[which], GENRE_POPULAR)
                            6 -> genreId("18", items[which], GENRE_POPULAR)
                            7 -> genreId("10751", items[which], GENRE_POPULAR)
                            8 -> genreId("14", items[which], GENRE_POPULAR)
                            9 -> genreId("878", items[which], GENRE_POPULAR)
                            10 -> genreId("27", items[which], GENRE_POPULAR)
                            11 -> genreId("53", items[which], GENRE_POPULAR)
                            12 -> genreId("9648", items[which], GENRE_POPULAR)
                            13 -> genreId("10752", items[which], GENRE_POPULAR)
                            14 -> genreId("80", items[which], GENRE_POPULAR)
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소", null)
                    .show()

            }
            R.id.genre_rated_movie -> {
                val items = resources.getStringArray(R.array.genre_item)
                AlertDialog.Builder(this, 2)
                    .setTitle("장르 선택")
                    .setItems(R.array.genre_item) { dialog, which ->
                        when(which) {
                            0 -> genreId("28", items[which], GENRE_RATED)
                            1 -> genreId("12", items[which], GENRE_RATED)
                            2 -> genreId("16", items[which], GENRE_RATED)
                            3 -> genreId("35", items[which], GENRE_RATED)
                            4 -> genreId("10749", items[which], GENRE_RATED)
                            5 -> genreId("99", items[which], GENRE_RATED)
                            6 -> genreId("18", items[which], GENRE_RATED)
                            7 -> genreId("10751", items[which], GENRE_RATED)
                            8 -> genreId("14", items[which], GENRE_RATED)
                            9 -> genreId("878", items[which], GENRE_RATED)
                            10 -> genreId("27", items[which], GENRE_RATED)
                            11 -> genreId("53", items[which], GENRE_RATED)
                            12 -> genreId("9648", items[which], GENRE_RATED)
                            13 -> genreId("10752", items[which], GENRE_RATED)
                            14 -> genreId("80", items[which], GENRE_RATED)
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
            R.id.favorite_movie -> {
                supportActionBar!!.title = "찜 목록"
                val favoriteRVAdapter = FavoriteRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)
                binding.mainContentItem.movieRecyclerView.run {
                    layoutManager = gridLayoutManager
                    setHasFixedSize(true)
                    adapter = favoriteRVAdapter
                }
                favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
                favoriteViewModel.allMovie.observe(this, Observer {
                    favoriteRVAdapter.setFavorite(it)
                })
            }
            R.id.movie_tv_app -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.devkproject.movieinfo3"))
                startActivity(intent);
            }
        }
        binding.mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                binding.mainDrawer.openDrawer(GravityCompat.START)
            }
            R.id.search_movie -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun genreId(genreId: String, genreName: String, sort_by: String) {
        supportActionBar!!.title = "장르 - $genreName"

        genreViewModel = ViewModelProvider(this, GenreViewModel.GenreViewModelFactory(apiService))
            .get(GenreViewModel::class.java)
        movieSetting()
        genreViewModel.getGenreView(genreId, sort_by).observe(this, Observer {
            movieAdapter.submitList(it)
        })
        genreViewModel.genreNetworkState().observe(this, Observer {
            binding.mainContentItem.movieProgressBar.visibility = if(genreViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.mainContentItem.movieErrorText.visibility = if(genreViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if(!genreViewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    fun searchQuery(query: String) {
        supportActionBar!!.title = "검색 : $query"

        searchViewModel = ViewModelProvider(this, SearchViewModel.SearchViewModelFactory(apiService))
            .get(SearchViewModel::class.java)
        movieSetting()
        searchViewModel.searchView(query).observe(this, Observer {
            movieAdapter.submitList(it)
        })
        searchViewModel.searchViewNetworkState().observe(this, Observer {
            binding.mainContentItem.movieProgressBar.visibility = if(searchViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if(!searchViewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem: MenuItem? = menu?.findItem(R.id.search_movie)
        searchView = searchItem?.actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery(newText.toString())
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onBackPressed() {
        val dialog = BackDialog(this)
        dialog.showDialog(this)
    }
}