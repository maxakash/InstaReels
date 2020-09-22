package com.weaponoid.instareels.views

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.weaponoid.instareels.persistance.Document
import com.weaponoid.instareels.utils.loadListImage
import com.weaponoid.instareels.viewmodels.HomeViewModel
import com.weaponoid.instareels.R
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.*
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.manager.*
import zlc.season.rxdownload4.task.Task

class Home : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var postData: HashMap<String, String>
    private lateinit var dpUrl: String
    private lateinit var fileUri: String

    var allReels: List<Document>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel()::class.java)

        viewModel.init(requireContext())

        downloadReel.setOnClickListener {
            validateUrl(url.text.toString())
            lastPost.visibility = View.VISIBLE

        }

        setLastPost()
    }


    private fun setLastPost() {

        allReels = viewModel.getAllDocuments()
        if (allReels?.isNotEmpty()!!) {
            val post = allReels!![0]
            lastPost.visibility = View.VISIBLE

            dp.loadListImage(post.dpUrl)

            listImage.loadListImage(post.videoUri)
            caption.text = post.caption
            handle.text = post.handle

            if (post.isVideo == "false") {
                isVideo.visibility = View.GONE
                isImage.visibility = View.VISIBLE
            }

        }
    }


    private fun validateUrl(url: String) {

        if (url.isEmpty()) {
            Snackbar.make(
                home_root,
                "URL cannot be empty",
                Snackbar.LENGTH_SHORT
            ).show()
        } else if (!viewModel.validateUrl(url)) {
            Snackbar.make(
                home_root,
                "Invalid URL entered",
                Snackbar.LENGTH_SHORT
            ).show()
        } else {

            circularProgressBar.visibility = View.VISIBLE
            circularProgressBar.indeterminateMode = true
            uiScope.launch {
                postData = withContext(Dispatchers.IO) { viewModel.getMediaUrl(url) }

                listImage.loadListImage(postData["thumbnail"].toString())
                handle.text = postData["handle"].toString()
                caption.text = postData["caption"].toString()

                withContext(Dispatchers.IO) {
                    dpUrl =
                        viewModel.getDP("https://www.instagram.com/${postData["handle"].toString()}/?__a=1")
                }

                if (postData["isVideo"].equals("false")) {
                    isVideo.visibility = View.GONE
                    isImage.visibility = View.VISIBLE
                }

                dp.loadListImage(dpUrl)

                val mediaUrl = postData["mediaUrl"]!!
                if (mediaUrl.isEmpty()) {
                    Snackbar.make(
                        home_root,
                        "Couldn't load the media file properly",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    download(mediaUrl)
                }

            }


        }
    }

    private fun download(url: String) {
        uiScope.launch {
            val path = requireContext().getExternalFilesDir("InstaReels")
            val disposable = Task(url = url, savePath = path!!.path).manager().apply {
                subscribe { status ->
                    when (status) {
                        is Completed -> {

                            circularProgressBar.visibility = View.GONE

                            downloadDp(dpUrl, postData["handle"].toString())
                            val file = url.file()
                            fileUri = Uri.fromFile(file).toString()

                        }
                        is Failed -> {
                            println("failed download $status")
                        }
                        is Started -> {
                            circularProgressBar.indeterminateMode = false
                        }

                        is Downloading -> {
                            //println(status.progress.percent())
                            circularProgressBar.apply {
                                progress = status.progress.percent().toFloat()
                                setProgressWithAnimation(65f, 1000)

                            }
                        }

                    }
                }

                start()
            }
        }

    }


    private fun downloadDp(url: String, username: String) {
        println("reached here")
        uiScope.launch {
            val path = requireContext().getExternalFilesDir("InstaReels")
            val disposable =
                Task(url = url, savePath = path!!.path, saveName = "$username.png").manager()
                    .apply {
                        subscribe { status ->
                            when (status) {
                                is Completed -> {
                                    println("dp downloaded")
                                    val newPost = Document()
                                    val dp = url.file()

                                    println(postData.toString())
                                    newPost.caption = postData["caption"].toString()
                                    newPost.hashtag = postData["hashtag"].toString()
                                    newPost.shareUrl = postData["shareUrl"].toString()
                                    newPost.videoUri = fileUri
                                    newPost.handle = postData["handle"].toString()
                                    newPost.dpUrl = Uri.fromFile(dp).toString()
                                    newPost.caption = postData["caption"].toString()
                                    newPost.isVideo = postData["isVideo"].toString()

                                    viewModel.saveDocument(newPost)


                                }
                                is Failed -> {
                                    println("failed download $status")
                                }

                            }
                        }

                        start()
                    }

        }

    }


}