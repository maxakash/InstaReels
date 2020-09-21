package com.whileloop.instareels.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import es.dmoral.toasty.Toasty

fun ImageView.loadListImage(uri: String?) {
    val options = RequestOptions()
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)

}


fun Context.infoToast(message: CharSequence) {

    Toasty.info(applicationContext, message, Toast.LENGTH_LONG, true).show()

}

fun Context.successToast(message: CharSequence) {

    Toasty.success(applicationContext, message, Toast.LENGTH_LONG, true).show()

}

fun Context.failedToast(message: CharSequence) {


    Toasty.error(applicationContext, message, Toast.LENGTH_LONG, true).show()

}


fun Activity.checkPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Activity.requestPermission(permission: String) {
    ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
}
