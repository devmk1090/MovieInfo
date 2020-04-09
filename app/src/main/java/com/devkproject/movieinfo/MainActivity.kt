package com.devkproject.movieinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.model.MovieThumb
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val movieService: TMDBInterface = TMDBClient.getClient()
        movieService.getPopularMovie(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setRVAdapter(it.movieList)
            }, {
                Log.d("MainActivity", "에러 : ${it.message}")
            })
    }

    private fun setRVAdapter(thumbList: ArrayList<MovieThumb>) {
        val mAdapter = PopularRVAdapter(thumbList, this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        popular_recyclerView.layoutManager = gridLayoutManager
        popular_recyclerView.setHasFixedSize(true)
        popular_recyclerView.adapter = mAdapter
    }
}
