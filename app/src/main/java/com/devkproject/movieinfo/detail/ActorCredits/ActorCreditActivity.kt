package com.devkproject.movieinfo.detail.ActorCredits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.databinding.ActivityActorCreditBinding

class ActorCreditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActorCreditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_actor_credit)
    }
}
