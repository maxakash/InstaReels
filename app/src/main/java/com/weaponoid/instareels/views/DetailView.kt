package com.weaponoid.instareels.views

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        viewModel.init(this)

        val fileUri = intent.getStringExtra("fileUri")
        val isVideo = intent.getStringExtra("isVideo")

        if (isVideo == "true") {
            detailVideo.apply {
                toggleControls()
                enableControls()
                enableSwipeGestures()
                setAutoPlay(true)
                setSource(Uri.parse(fileUri))
            }
        } else {
            detailVideo.visibility = View.GONE
            detailImage.visibility = View.VISIBLE
            detailImage.setImageURI(Uri.parse(fileUri))
        }

        //  println(fileUri)

    }


    fun onClick(view: View) {

        when (view.id) {

            R.id.backButton -> {
                onBackPressed()
            }

            R.id.captionButton -> {
                viewModel.copyCaption(intent.getStringExtra("caption").toString(), this)
            }

            R.id.hashtagButton -> {
                viewModel.copyHashtag(intent.getStringExtra("hashtag").toString(), this)
            }

            R.id.repostButton -> {
                viewModel.rePost(
                    intent.getStringExtra("fileName").toString(),
                    this,
                    intent.getStringExtra("isVideo").toString()
                )
            }

            R.id.openPostButton -> {
                viewModel.openPost(intent.getStringExtra("postUrl").toString(), this)
            }

            R.id.shareButton -> {
                viewModel.share(intent.getStringExtra("fileName").toString(), this)
            }

            R.id.deleteButton -> {

                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete this post?")
                    .setNegativeButton(
                        "Cancel"
                    ) { dialogBox: DialogInterface, id: Int -> dialogBox.dismiss() }

                    .setPositiveButton(
                        "Yes"
                    ) { _: DialogInterface?, id: Int ->
                        val docId = intent.getIntExtra("id", 0)
                        if (docId != 0) {
                            viewModel.deleteDocument(
                                docId,
                                intent.getStringExtra("fileName").toString(),
                                this
                            )
                        }

                        finish()
                    }

                    .show()
            }


        }
    }


    override fun onPause() {
        super.onPause()
        if (detailVideo.isPlaying) {
            detailVideo.stop()
        }
    }


}