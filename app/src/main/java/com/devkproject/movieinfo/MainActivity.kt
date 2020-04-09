package com.devkproject.movieinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.paging.TMDBPagedListRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel
    lateinit var tmdbPagedListRepository: TMDBPagedListRepository

    private var first_time : Long = 0
    private var second_time : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: TMDBInterface = TMDBClient.getClient()
        tmdbPagedListRepository = TMDBPagedListRepository(apiService)
        viewModel = getViewModel()

        val mAdapter = PopularPagedListRVAdapter( this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        popular_recyclerView.layoutManager = gridLayoutManager
        popular_recyclerView.setHasFixedSize(true)
        popular_recyclerView.adapter = mAdapter

        viewModel.tmdbPagedList.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun getViewModel(): MainViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(tmdbPagedListRepository) as T
            }
        })[MainViewModel::class.java]
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
