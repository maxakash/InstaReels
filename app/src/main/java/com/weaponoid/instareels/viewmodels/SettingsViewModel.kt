package com.weaponoid.instareels.viewmodels

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.RatingBar
import androidx.lifecycle.ViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.weaponoid.instareels.R
import com.weaponoid.instareels.utils.successToast

class SettingsViewModel : ViewModel() {

    fun rateApp(activity: Activity) {
        val inflater = activity.layoutInflater.inflate(R.layout.rate_app_dialog, null)
        val ratings = inflater.findViewById<RatingBar>(R.id.ratings)

        MaterialAlertDialogBuilder(activity)
            .setView(inflater)
            .setPositiveButton("Rate") { _, _ ->
                if (ratings.rating.toInt() == 5) {
                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.weaponoid.instareels")
                        )
                    )
                } else {
                    activity.successToast("Thanks for your feedback.")
                }

            }
            .setNegativeButton("Later") { _, _ ->
            }
            .show()
    }

    fun disclaimer(activity: Activity){



        MaterialAlertDialogBuilder(activity)
            .setTitle("Disclaimer")
            .setMessage(activity.getString(R.string.disclaimer))
            .setPositiveButton("Ok") { dialog, which ->

            }

            .show()

    }


    fun privacy(activity: Activity){
        MaterialAlertDialogBuilder(activity)
            .setTitle("Privacy Policy")
            .setMessage(activity.getString(R.string.privacy))
            .setPositiveButton("Ok") { dialog, which ->

            }

            .show()
    }


}