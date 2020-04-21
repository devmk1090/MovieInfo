package com.devkproject.movieinfo.detail.credits


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBCast
import kotlinx.android.synthetic.main.fragment_credits.*

class CreditsFragment : Fragment() {

    private lateinit var creditsViewModel: CreditsViewModel
    private lateinit var creditsRepository: CreditsRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val movieId: Int = activity!!.intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()
        creditsRepository = CreditsRepository(apiService)
        creditsViewModel = getViewModel(movieId)

        creditsViewModel.creditsMovie.observe(viewLifecycleOwner, Observer {
            setRVAdapter(it.cast)
        })
        return inflater.inflate(R.layout.fragment_credits, container, false)
    }

    private fun setRVAdapter(item: ArrayList<TMDBCast>) {
        val creditsRVAdapter = CreditsRVAdapter(item)
        credits_recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        credits_recyclerView.setHasFixedSize(true)
        credits_recyclerView.adapter = creditsRVAdapter
    }

    private fun getViewModel(movieId: Int): CreditsViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CreditsViewModel(
                    creditsRepository,
                    movieId
                ) as T
            }
        })[CreditsViewModel::class.java]
    }
}
