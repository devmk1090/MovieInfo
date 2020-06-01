package com.devkproject.movieinfo

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.devkproject.movieinfo.api.GENRE_POPULAR
import com.devkproject.movieinfo.toprated.TopRatedRepository
import com.devkproject.movieinfo.toprated.TopRatedViewModel
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.api.TMDBClient
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
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.main_drawer.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val apiService: TMDBInterface = TMDBClient.getClient()

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

    private var movieAdapter = PagedListRVAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_drawer)

        MobileAds.initialize(this) {}
        mAdView = this.findViewById(R.id.adView_main)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        main_drawer_navigationView.setNavigationItemSelectedListener(this)
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
        movie_recyclerView.run {
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
            movie_progress_bar.visibility = if(nowPlayingViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            movie_error_text.visibility = if(nowPlayingViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
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
                    movie_progress_bar.visibility = if(popularViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                    movie_error_text.visibility = if(popularViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
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
                    movie_progress_bar.visibility = if(topRatedViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                    movie_error_text.visibility = if(topRatedViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
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
                    movie_progress_bar.visibility = if(upcomingViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                    movie_error_text.visibility = if(upcomingViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
                    if(!upcomingViewModel.listIsEmpty()) {
                        movieAdapter.setNetworkState(it)
                    }
                })
            }
            R.id.genre_popular_movie -> {
                AlertDialog.Builder(this, 3)
                    .setTitle("장르 선택")
                    .setItems(R.array.genre_item) { dialog, which ->
                        when(which) {
                            0 -> genreId("28", which.toString(), GENRE_POPULAR)
//                            1 -> genreId("12", [1],GENRE_POPULAR)
//                            2 -> genreId("16", , GENRE_POPULAR)
//                            3 -> genreId("35", , GENRE_POPULAR)
//                            4 -> genreId("10749", , GENRE_POPULAR)
//                            5 -> genreId("99", , GENRE_POPULAR)
//                            6 -> genreId("18", , GENRE_POPULAR)
//                            7 -> genreId("10751", , GENRE_POPULAR)
//                            8 -> genreId("14", , GENRE_POPULAR)
//                            9 -> genreId("878", , GENRE_POPULAR)
//                            10 -> genreId("27", , GENRE_POPULAR)
//                            11 -> genreId("53", , GENRE_POPULAR)
//                            12 -> genreId("9648", , GENRE_POPULAR)
//                            13 -> genreId("10752", , GENRE_POPULAR)
//                            14 -> genreId("80", , GENRE_POPULAR)
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소", null)
                    .show()

            }
            R.id.genre_rated_movie -> {

                AlertDialog.Builder(this, 3)
                    .setTitle("장르 선택")
                    .setItems(R.array.genre_item) { dialog, which ->
                        when(which) {
                            0 -> genreId("28", "액션", "vote_average.desc")
                            1 -> genreId("12", "모험", "vote_average.desc")
                            2 -> genreId("16", "애니메이션", "vote_average.desc")
                            3 -> genreId("35", "코미디", "vote_average.desc")
                            4 -> genreId("10749", "로맨스", "vote_average.desc")
                            5 -> genreId("99", "다큐멘터리", "vote_average.desc")
                            6 -> genreId("18", "드라마", "vote_average.desc")
                            7 -> genreId("10751", "가족", "vote_average.desc")
                            8 -> genreId("14", "판타지", "vote_average.desc")
                            9 -> genreId("878", "SF", "vote_average.desc")
                            10 -> genreId("27", "공포", "vote_average.desc")
                            11 -> genreId("53", "스릴러", "vote_average.desc")
                            12 -> genreId("9648", "미스터리", "vote_average.desc")
                            13 -> genreId("10752", "전쟁", "vote_average.desc")
                            14 -> genreId("80", "범죄", "vote_average.desc")
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
                movie_recyclerView.run {
                    layoutManager = gridLayoutManager
                    setHasFixedSize(true)
                    adapter = favoriteRVAdapter
                }
                favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
                favoriteViewModel.allMovie.observe(this, Observer {
                    favoriteRVAdapter.setFavorite(it)
                })
            }
        }
        main_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                main_drawer.openDrawer(GravityCompat.START)
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
            movie_progress_bar.visibility = if(genreViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            movie_error_text.visibility = if(genreViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
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
            movie_progress_bar.visibility = if(searchViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
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
        if(!searchView!!.isIconified) {
            searchView!!.isIconified = true
            searchView!!.onActionViewCollapsed()
        } else if(main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START)
        }
        else {
            secondTime = System.currentTimeMillis()
            if(secondTime - firstTime < 2000) {
                super.onBackPressed()
                finishAffinity()
            } else Toast.makeText(this,"한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            firstTime = System.currentTimeMillis()
        }
    }
}