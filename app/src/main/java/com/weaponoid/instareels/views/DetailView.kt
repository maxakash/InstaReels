package com.weaponoid.instareels.views

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.weaponoid.instareels.R
import com.weaponoid.instareels.viewmodels.DetailViewViewModel
import kotlinx.android.synthetic.main.activity_detail_view.*


class DetailView : AppCompatActivity() {

    private lateinit var viewModel: DetailViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)

        bar.elevation = 0f

        viewModel = ViewModelProvider(this).get(DetailViewViewModel::class.java)

        val fileUri = intent.getStringExtra("fileUri")
        val isVideo = intent.getStringExtra("isVideo")

        if(isVideo=="true"){
            detailVideo.apply {
                toggleControls()
                enableControls()
                enableSwipeGestures()
                setAutoPlay(true)
                setSource(Uri.parse(fileUri))
            }
        }else{
            detailVideo.visibility = View.GONE
            detailImage.visibility = View.VISIBLE
            detailImage.setImageURI(Uri.parse(fileUri))
        }

        println(fileUri)



    }


//    override fun onDestroy() {
//        super.onDestroy()
//        detailVideo.stop()
//    }
//
//
//    override fun onPause() {
//        super.onPause()
//        detailVideo.stop()
//    }


}