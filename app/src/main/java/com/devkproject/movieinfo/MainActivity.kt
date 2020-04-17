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
import com.devkproject.movieinfo.popular.PopularViewModel
import com.devkproject.movieinfo.popular.PopularRepository
import com.devkproject.movieinfo.search.SearchViewModel
import com.devkproject.movieinfo.upcoming.UpcomigViewModel
import com.devkproject.movieinfo.upcoming.UpcomingRepository
import com.google.android.material.navigation.NavigationView
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
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

    private var first_time : Long = 0
    private var second_time : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_drawer)

        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //드로어를 꺼낼 홈 버튼 활성화
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp) //홈버튼 이미지 변경

        main_drawer_navigationView.setNavigationItemSelectedListener(this)

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
                supportActionBar!!.title = "높은 평점"

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
            R.id.genre_movie -> {
                val items = arrayOf("액션", "모험", "애니메이션", "코미디", "범죄", "다큐멘터리", "드라마",
                    "가족", "판타지", "SF", "공포", "스릴러", "전쟁", "로맨스")
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("장르 선택")
                    .setItems(items) { dialog, which ->
                        when(which) {
                            0 -> genreId("28", "액션")
                            1 -> genreId("12", "모험")
                            2 -> genreId("16", "애니메이션")
                            3 -> genreId("35", "코미디")
                            4 -> genreId("80", "범죄")
                            5 -> genreId("99", "다큐멘터리")
                            6 -> genreId("18", "드라마")
                            7 -> genreId("10751", "가족")
                            8 -> genreId("14", "판타지")
                            9 -> genreId("878", "SF")
                            10 -> genreId("27", "공포")
                            11 -> genreId("53", "스릴러")
                            12 -> genreId("10752", "전쟁")
                            13 -> genreId("10749", "로맨스")
                        }
                        dialog.dismiss()
                    }
                alertDialog.show()
//                apiService.getGenreMovie().enqueue(object : Callback<TMDBGenre> {
//                    override fun onResponse(call: Call<TMDBGenre>, response: Response<TMDBGenre>) {
//                        val body = response.body()
//                        for (i in body!!.genres) {
//                            val items: CharSequence
//                            sb.append(i.name + i.id)
//                            println(sb.toString())
//                        }
//                    }
//                    override fun onFailure(call: Call<TMDBGenre>, t: Throwable) {
//                        Log.e("MainActivity", "실패 : ${t.message}")
//                    }
//                })
//                val alertDialog = AlertDialog.Builder(this)
//                    .setTitle("테스트")
//                alertDialog.show()
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

    private fun genreId(genreId: String, genreName: String) {
        supportActionBar!!.title = "장르 : $genreName"

        genreViewModel = getGenreViewModel()
        val genreAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        movie_recyclerView.layoutManager = gridLayoutManager
        movie_recyclerView.setHasFixedSize(true)
        movie_recyclerView.adapter = genreAdapter

        genreViewModel.getGenreView(genreId).observe(this, Observer {
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
        val menuInflater = menuInflater.inflate(R.menu.toolbar_item, menu)
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