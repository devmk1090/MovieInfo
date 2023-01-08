package com.devkproject.movieinfo

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.dialog_back_ad.*

class BackDialog(context: Context) {
    private val dialog = Dialog(context)

    fun showDialog(activity: Activity) {
        dialog.run {
            setContentView(R.layout.dialog_back_ad)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            dialog_no.setOnClickListener { dismiss() }
            dialog_yes.setOnClickListener {
                dialog.dismiss()
                activity.finishAffinity() }

            val adRequest = AdRequest.Builder().build()
            dialog_adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                }
            }
            dialog_adView.loadAd(adRequest)

            show()
        }
    }
}