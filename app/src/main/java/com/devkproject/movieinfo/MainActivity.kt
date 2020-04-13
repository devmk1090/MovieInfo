package com.devkproject.movieinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.devkproject.movieinfo.toprated.TopRatedRepository
import com.devkproject.movieinfo.toprated.TopRatedViewModel
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.popular.PopularViewModel
import com.devkproject.movieinfo.popular.PopularRepository
import com.devkproject.movieinfo.upcoming.UpcomigViewModel
import com.devkproject.movieinfo.upcoming.UpcomingRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var popularViewModel: PopularViewModel
    lateinit var tmdbRepository: PopularRepository

    private lateinit var topRatedViewModel: TopRatedViewModel
    lateinit var tmdbTopRatedRepository: TopRatedRepository

    private lateinit var upcomingViewModel: UpcomigViewModel
    lateinit var upcomingRepository: UpcomingRepository

    private var first_time : Long = 0
    private var second_time : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        val apiService: TMDBInterface = TMDBClient.getClient()
        upcomingRepository = UpcomingRepository(apiService)
        upcomingViewModel = getUpcomingViewModel()

        val upcomingAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        popular_recyclerView.layoutManager = gridLayoutManager
        popular_recyclerView.setHasFixedSize(true)
        popular_recyclerView.adapter = upcomingAdapter

        upcomingViewModel.upcomingPagedList.observe(this, Observer {
            upcomingAdapter.submitList(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                search_txt.visibility = View.GONE
                return true
            }
            R.id.search_movie -> {
                search_txt.visibility = View.VISIBLE
                val name = search_txt.text.toString()
                return true
            }
            R.id.popular_movie -> {
                val apiService: TMDBInterface = TMDBClient.getClient()
                tmdbRepository =
                    PopularRepository(apiService)
                popularViewModel = getPopularViewModel()

                val mAdapter = PagedListRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)

                popular_recyclerView.layoutManager = gridLayoutManager
                popular_recyclerView.setHasFixedSize(true)
                popular_recyclerView.adapter = mAdapter

                popularViewModel.tmdbPagedList.observe(this, Observer {
                    mAdapter.submitList(it)
                })
                return true
            }
            R.id.topRated_movie -> {
                val apiService: TMDBInterface = TMDBClient.getClient()
                tmdbTopRatedRepository =
                    TopRatedRepository(apiService)
                topRatedViewModel = getTopRatedViewModel()

                val topRatedAdapter =
                    PagedListRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)

                popular_recyclerView.layoutManager = gridLayoutManager
                popular_recyclerView.setHasFixedSize(true)
                popular_recyclerView.adapter = topRatedAdapter

                topRatedViewModel.tmdbTopRatedPagedList.observe(this, Observer {
                    topRatedAdapter.submitList(it)
                })
                return true
            }
            R.id.upcoming_movie -> {
                val apiService: TMDBInterface = TMDBClient.getClient()
                upcomingRepository = UpcomingRepository(apiService)
                upcomingViewModel = getUpcomingViewModel()

                val upcomingAdapter = PagedListRVAdapter(this)
                val gridLayoutManager = GridLayoutManager(this, 3)

                popular_recyclerView.layoutManager = gridLayoutManager
                popular_recyclerView.setHasFixedSize(true)
                popular_recyclerView.adapter = upcomingAdapter

                upcomingViewModel.upcomingPagedList.observe(this, Observer {
                    upcomingAdapter.submitList(it)
                })
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater.inflate(R.menu.toolbar_item, menu)
        return super.onCreateOptionsMenu(menu)
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

    override fun onBackPressed() {
        second_time = System.currentTimeMillis()
        if(second_time - first_time < 2000) {
            super.onBackPressed()
            finishAffinity()
        } else Toast.makeText(this,"한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        first_time = System.currentTimeMillis()
    }
}
