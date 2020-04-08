package com.devkproject.movieinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devkproject.movieinfo.api.MovieInterface
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.model.MovieResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val API_KEY = "4140785408ae0d33bd7a2220a28fa0e2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movie_btn.setOnClickListener {
            val movieService: MovieInterface = TMDBClient.getMovieClient()
            movieService.listOfMovies(API_KEY, 1).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    Log.d("MainActivity", "성공")
                    val results = response.body()
                    for (i in results!!.movieList) {
                        Log.d("MainActivity", i.budget.toString())
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("MainActivity", "실패 : $t.message")
                }


            })
        }
    }
}
