package com.devkproject.movieinfo

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.devkproject.movieinfo.databinding.DialogBackAdBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest

class BackDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var binding: DialogBackAdBinding

    fun showDialog(activity: Activity) {
        dialog.run {
            binding = DialogBackAdBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            binding.dialogNo.setOnClickListener { dismiss() }
            binding.dialogYes.setOnClickListener {
                dialog.dismiss()
                activity.finishAffinity() }

            val adRequest = AdRequest.Builder().build()
            binding.dialogAdView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                }
            }
            binding.dialogAdView.loadAd(adRequest)

            show()
        }
    }
}