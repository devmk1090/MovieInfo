package com.devkproject.movieinfo.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import kotlinx.android.synthetic.main.fragment_synopsis.*

class SynopsisFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val movieId: Int = activity!!.intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()
        detailRepository = DetailRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            bindUI(it)
        })
        return inflater.inflate(R.layout.fragment_synopsis, container, false)
    }

    private fun bindUI(it: TMDBDetail) {
        synopsis_overview.text = it.overview

    }

    private fun getViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(detailRepository, movieId) as T
            }
        })[DetailViewModel::class.java]
    }
}
