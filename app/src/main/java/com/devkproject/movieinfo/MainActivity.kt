package com.devkproject.movieinfo

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.devkproject.movieinfo.toprated.TopRatedRepository
import com.devkproject.movieinfo.toprated.TopRatedViewModel
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.genre.GenreViewModel
import com.devkproject.movieinfo.model.TMDBGenre
import com.devkproject.movieinfo.now_playing.NowPlayingViewModel
import com.devkproject.movieinfo.popular.PopularViewModel
import com.devkproject.movieinfo.popular.PopularRepository
import com.devkproject.movieinfo.search.SearchViewModel
import com.devkproject.movieinfo.upcoming.UpcomigViewModel
import com.devkproject.movieinfo.upcoming.UpcomingRepository
import com.google.android.material.navigation.NavigationView
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.main_drawer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val apiService: TMDBInterface = TMDBClient.getClient()

    private lateinit var popularViewModel: PopularViewModel
    lateinit var tmdbRepository: PopularRepository

    private lateinit var topRatedViewModel: TopRatedViewModel
    lateinit var tmdbTopRatedRepository: TopRatedRepository

    private lateinit var upcomingViewModel: UpcomigViewModel
    lateinit var upcomingRepository: UpcomingRepository

    private lateinit var searchViewModel: SearchViewModel
    private var searchView: SearchView? = null

    private lateinit var genreViewModel: GenreViewModel

    private lateinit var nowPlayingViewModel: NowPlayingViewModel

    private var first_time : Long = 0
    private var second_time : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_drawer)

        main_drawer_navigationView.setNavigationItemSelectedListener(this)
        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //드로어를 꺼낼 홈 버튼 활성화
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp) //홈버튼 이미지 변경
        supportActionBar!!.title = "현재 상영작"

        nowPlayingViewModel = getNowPlayingViewModel()
        val nowPlayingAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        movie_recyclerView.layoutManager = gridLayoutManager
        movie_recyclerView.setHasFixedSize(true)
        movie_recyclerView.adapter = nowPlayingAdapter

        nowPlayingViewModel.nowPlayingView().observe(this, Observer {
            nowPlayingAdapter.submitList(it)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.popular_movie -> {
                supportActionBar!!.title = "인기 영화"

                tmdbRepository = PopularRepository(apiService)
                popularViewModel = getPopularViewModel()

                val popularAdapter = PagedListRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)

                movie_recyclerView.layoutManager = gridLayoutManager
                movie_recyclerView.setHasFixedSize(true)
                movie_recyclerView.adapter = popularAdapter

                popularViewModel.tmdbPagedList.observe(this, Observer {
                    popularAdapter.submitList(it)
                })
            }
            R.id.topRated_movie -> {
                supportActionBar!!.title = "높은 평점순"

                tmdbTopRatedRepository = TopRatedRepository(apiService)
                topRatedViewModel = getTopRatedViewModel()

                val topRatedAdapter =
                    PagedListRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)

                movie_recyclerView.layoutManager = gridLayoutManager
                movie_recyclerView.setHasFixedSize(true)
                movie_recyclerView.adapter = topRatedAdapter

                topRatedViewModel.tmdbTopRatedPagedList.observe(this, Observer {
                    topRatedAdapter.submitList(it)
                })
            }
            R.id.nowPlaying_movie -> {
                supportActionBar!!.title = "현재 상영작"

                nowPlayingViewModel = getNowPlayingViewModel()
                val nowPlayingAdapter = PagedListRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)

                movie_recyclerView.layoutManager = gridLayoutManager
                movie_recyclerView.setHasFixedSize(true)
                movie_recyclerView.adapter = nowPlayingAdapter

                nowPlayingViewModel.nowPlayingView().observe(this, Observer {
                    nowPlayingAdapter.submitList(it)
                })
            }
            R.id.upcoming_movie -> {
                supportActionBar!!.title = "개봉 예정"

                upcomingRepository = UpcomingRepository(apiService)
                upcomingViewModel = getUpcomingViewModel()

                val upcomingAdapter = PagedListRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)

                movie_recyclerView.layoutManager = gridLayoutManager
                movie_recyclerView.setHasFixedSize(true)
                movie_recyclerView.adapter = upcomingAdapter

                upcomingViewModel.upcomingPagedList.observe(this, Observer {
                    upcomingAdapter.submitList(it)
                })
            }
            R.id.genre_popular_movie -> {
                val items = arrayOf("액션", "모험", "애니메이션", "코미디", "범죄", "다큐멘터리", "드라마",
                    "가족", "판타지", "SF", "공포", "스릴러", "미스터리", "전쟁", "로맨스")
                AlertDialog.Builder(this, 3)
                    .setTitle("장르 선택")
                    .setItems(items) { dialog, which ->
                        when(which) {
                            0 -> genreId("28", "액션", "popularity.desc")
                            1 -> genreId("12", "모험","popularity.desc")
                            2 -> genreId("16", "애니메이션", "popularity.desc")
                            3 -> genreId("35", "코미디", "popularity.desc")
                            4 -> genreId("80", "범죄", "popularity.desc")
                            5 -> genreId("99", "다큐멘터리", "popularity.desc")
                            6 -> genreId("18", "드라마", "popularity.desc")
                            7 -> genreId("10751", "가족", "popularity.desc")
                            8 -> genreId("14", "판타지", "popularity.desc")
                            9 -> genreId("878", "SF", "popularity.desc")
                            10 -> genreId("27", "공포", "popularity.desc")
                            11 -> genreId("53", "스릴러", "popularity.desc")
                            12 -> genreId("9648", "미스터리", "popularity.desc")
                            13 -> genreId("10752", "전쟁", "popularity.desc")
                            14 -> genreId("10749", "로맨스", "popularity.desc")
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
            R.id.genre_rated_movie -> {
                val items = arrayOf("액션", "모험", "애니메이션", "코미디", "범죄", "다큐멘터리", "드라마",
                    "가족", "판타지", "SF", "공포", "스릴러", "미스터리", "전쟁", "로맨스")
                AlertDialog.Builder(this, 3)
                    .setTitle("장르 선택")
                    .setItems(items) { dialog, which ->
                        when(which) {
                            0 -> genreId("28", "액션", "vote_average.desc")
                            1 -> genreId("12", "모험", "vote_average.desc")
                            2 -> genreId("16", "애니메이션", "vote_average.desc")
                            3 -> genreId("35", "코미디", "vote_average.desc")
                            4 -> genreId("80", "범죄", "vote_average.desc")
                            5 -> genreId("99", "다큐멘터리", "vote_average.desc")
                            6 -> genreId("18", "드라마", "vote_average.desc")
                            7 -> genreId("10751", "가족", "vote_average.desc")
                            8 -> genreId("14", "판타지", "vote_average.desc")
                            9 -> genreId("878", "SF", "vote_average.desc")
                            10 -> genreId("27", "공포", "vote_average.desc")
                            11 -> genreId("53", "스릴러", "vote_average.desc")
                            12 -> genreId("9648", "미스터리", "vote_average.desc")
                            13 -> genreId("10752", "전쟁", "vote_average.desc")
                            14 -> genreId("10749", "로맨스", "vote_average.desc")
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소", null)
                    .show()
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

        genreViewModel = getGenreViewModel()
        val genreAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        movie_recyclerView.layoutManager = gridLayoutManager
        movie_recyclerView.setHasFixedSize(true)
        movie_recyclerView.adapter = genreAdapter

        genreViewModel.getGenreView(genreId, sort_by).observe(this, Observer {
            genreAdapter.submitList(it)
        })
    }
    fun searchQuery(query: String) {
        supportActionBar!!.title = "검색 : $query"

        searchViewModel = getSearchViewModel()
        val searchAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        movie_recyclerView.layoutManager = gridLayoutManager
        movie_recyclerView.setHasFixedSize(true)
        movie_recyclerView.adapter = searchAdapter

        searchViewModel.searchView(query).observe(this, Observer {
            searchAdapter.submitList(it)
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

    private fun getPopularViewModel(): PopularViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PopularViewModel(tmdbRepository) as T
            }
        })[PopularViewModel::class.java]
    }

    private fun getTopRatedViewModel(): TopRatedViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TopRatedViewModel(tmdbTopRatedRepository) as T
            }
        })[TopRatedViewModel::class.java]
    }

    private fun getUpcomingViewModel(): UpcomigViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UpcomigViewModel(upcomingRepository) as T
            }
        })[UpcomigViewModel::class.java]
    }

    private fun getSearchViewModel(): SearchViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SearchViewModel(apiService) as T
            }
        })[SearchViewModel::class.java]
    }

    private fun getGenreViewModel(): GenreViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GenreViewModel(apiService) as T
            }
        })[GenreViewModel::class.java]
    }

    private fun getNowPlayingViewModel(): NowPlayingViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NowPlayingViewModel(apiService) as T
            }

        })[NowPlayingViewModel::class.java]
    }

    override fun onBackPressed() {
        if(!searchView!!.isIconified) {
            searchView!!.isIconified = true
            searchView!!.onActionViewCollapsed()
        } else if(main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START)
        }
        else {
            second_time = System.currentTimeMillis()
            if(second_time - first_time < 2000) {
                super.onBackPressed()
                finishAffinity()
            } else Toast.makeText(this,"한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            first_time = System.currentTimeMillis()
        }
    }
}