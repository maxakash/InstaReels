package com.weaponoid.instareels.views

import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weaponoid.instareels.R
import com.weaponoid.instareels.persistance.Document
import com.weaponoid.instareels.utils.failedToast
import com.weaponoid.instareels.utils.infoToast
import com.weaponoid.instareels.utils.loadListImage
import com.weaponoid.instareels.utils.successToast
import com.weaponoid.instareels.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.*
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.manager.*
import zlc.season.rxdownload4.task.Task
import java.io.File


class Home : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var postData: HashMap<String, String>
    private lateinit var dpUrl: String
    private lateinit var fileUri: String
    private lateinit var dpUri: String
    private lateinit var disposable1: TaskManager
    private lateinit var disposable2: TaskManager
    var allReels: List<Document>? = null
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var lastPostData: Document
    private lateinit var fileName: String
    private lateinit var postUrl: String

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

        clipboardManager =
            (requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)!!

        downloadReel.setOnClickListener {
            validateUrl(editText.text.toString())
            lastPost.visibility = View.VISIBLE

        }

        setLastPost()

        getCopiedData()

        onClick()

    }


    private fun setLastPost() {

        allReels = viewModel.getAllDocuments()
        if (allReels?.isNotEmpty()!!) {
            val post = allReels!![0]
            lastPost.visibility = View.VISIBLE

            dp.loadListImage(post.dpUrl)
            postImage.loadListImage(post.videoUri)
            caption.text = post.caption
            handle.text = post.handle

            if (post.isVideo == "false") {
                isVideo.visibility = View.GONE
                isImage.visibility = View.VISIBLE
            }
            lastPostData = post
        }
    }

    private fun getCopiedData() {
        val clipData: ClipData? = clipboardManager.primaryClip
        val item = clipData?.getItemAt(0)
        val url = item?.text.toString()

        if (url != " " && url.startsWith("https://") || url.startsWith("http://")) {
            requireContext().infoToast("Validating copied link")
            validateUrl(url)
        }
    }

    private fun validateUrl(url: String) {

        if (url.isEmpty()) {
            requireContext().failedToast("URL cannot be empty")
        } else if (!viewModel.validateUrl(url)) {
            requireContext().infoToast("Invalid link entered")
        } else {

            postUrl = url
            lastPost.visibility = View.VISIBLE
            editText.clearFocus()
            editText.requestFocus()
            circularProgressBar.visibility = View.VISIBLE
            circularProgressBar.progress = 0f
            uiScope.launch {
                postData = withContext(Dispatchers.IO) { viewModel.getMediaUrl(url) }

                val mediaUrl = postData["mediaUrl"]!!

                if (mediaUrl == "") {
                    requireContext().failedToast("Couldn't load the media file properly")
                } else {
                    postImage.loadListImage(postData["thumbnail"].toString())
                    handle.text = postData["handle"].toString()
                    caption.text = postData["caption"].toString()


                    Log.i(
                        "akash",
                        "https://www.instagram.com/${postData["handle"].toString()}/?__a=1"
                    )

                    dpUrl =
                        withContext(Dispatchers.IO) { viewModel.getDP("https://www.instagram.com/${postData["handle"].toString()}/?__a=1") }

                    if (postData["isVideo"].equals("false")) {
                        isVideo.visibility = View.GONE
                        isImage.visibility = View.VISIBLE
                    }

                    dp.loadListImage(dpUrl)

                    download(mediaUrl)
                }

            }


        }
    }

    private fun download(url: String) {
        uiScope.launch {
            val path = requireContext().getExternalFilesDir("temp")
            disposable1 = Task(url = url, savePath = path!!.path).manager().apply {
                subscribe { status ->
                    when (status) {
                        is Completed -> {

                            circularProgressBar.visibility = View.GONE

                            downloadDp(dpUrl, postData["handle"].toString())

                            val videoFile = File(
                                requireContext().getExternalFilesDir("InstaReels"),
                                url.file().name
                            )

                            fileName = videoFile.name

                            println(fileName)
                            url.file().copyTo(videoFile, true)
                            fileUri = Uri.fromFile(videoFile).toString()

                            postImage.loadListImage(fileUri)

                        }
                        is Failed -> {
                            println("failed download $status")
                        }

                        is Downloading -> {
                            circularProgressBar.apply {
                                progress = status.progress.percent().toFloat()

                            }
                        }

                    }
                }

                start()
            }
        }

    }


    private fun downloadDp(url: String, username: String) {
        uiScope.launch {
            val path = requireContext().getExternalFilesDir("temp")
            disposable2 =
                Task(url = url, savePath = path!!.path, saveName = "$username.png").manager()
                    .apply {
                        subscribe { status ->
                            when (status) {
                                is Completed -> {
                                    println("dp downloaded")
                                    val dpFile = File(
                                        requireContext().getExternalFilesDir("InstaReels"),
                                        "$username.png"
                                    )
                                    url.file().copyTo(dpFile, true)

                                    dpUri = Uri.fromFile(dpFile).toString()
                                    saveDoc()

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


    private fun saveDoc() {
        val newPost = Document()
        newPost.caption = postData["caption"].toString()
        newPost.hashtag = postData["hashtag"].toString()
        newPost.shareUrl = postData["shareUrl"].toString()
        newPost.videoUri = fileUri
        newPost.handle = postData["handle"].toString()
        newPost.dpUrl = dpUri
        newPost.postUrl = postUrl
        newPost.caption = postData["caption"].toString()
        newPost.isVideo = postData["isVideo"].toString()
        newPost.fileName = fileName

        viewModel.saveDocument(newPost)

        editText.text.clear()
        editText.clearFocus()
        requireContext().successToast("Download Completed")


        disposable1.delete()
        disposable2.delete()


        val clip = ClipData.newPlainText("label", "")
        clipboardManager.setPrimaryClip(clip)
    }


    private fun onClick() {

        captionButton.setOnClickListener {

            allReels = viewModel.getAllDocuments()
            if (allReels?.isNotEmpty()!!) {
                lastPostData = allReels!![0]
            }

            if (this::postData.isInitialized) {
                val clip = ClipData.newPlainText("label", postData["caption"].toString())
                clipboardManager.setPrimaryClip(clip)
            } else if (this::lastPostData.isInitialized) {
                val clip = ClipData.newPlainText("label", lastPostData.caption)
                clipboardManager.setPrimaryClip(clip)
            }

            requireContext().successToast("Caption copied to clipboard.")
        }

        hashtagButton.setOnClickListener {

            allReels = viewModel.getAllDocuments()
            if (allReels?.isNotEmpty()!!) {
                lastPostData = allReels!![0]
            }

            if (this::postData.isInitialized) {
                val clip = ClipData.newPlainText("label", postData["hashtag"].toString())
                clipboardManager.setPrimaryClip(clip)
            } else if (this::lastPostData.isInitialized) {
                val clip = ClipData.newPlainText("label", lastPostData.hashtag)
                clipboardManager.setPrimaryClip(clip)
            }
            requireContext().successToast("Hashtags copied to clipboard.")
        }

        shareButton.setOnClickListener {

            allReels = viewModel.getAllDocuments()
            if (allReels?.isNotEmpty()!!) {
                lastPostData = allReels!![0]
            }

            var name = ""

            if (this::lastPostData.isInitialized) {
                name = lastPostData.fileName

            } else if (this::postData.isInitialized) {

                name = fileName

            }
            val intent = Intent(Intent.ACTION_SEND)
            val sd = requireContext().getExternalFilesDir("InstaReels")
            val toShare = File(sd, name)

            println(toShare.exists())
            val authority = "com.weaponoid.instareels.provider"
            val contentUri = FileProvider.getUriForFile(requireContext(), authority, toShare)
            intent.putExtra(Intent.EXTRA_STREAM, contentUri)

            intent.type = "image/*"

            val chooser = Intent.createChooser(intent, "Share File")

            val resInfoList: List<ResolveInfo> = requireActivity().packageManager
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(
                    packageName,
                    contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            startActivity(chooser)
        }

        repostButton.setOnClickListener {
            allReels = viewModel.getAllDocuments()
            if (allReels?.isNotEmpty()!!) {
                lastPostData = allReels!![0]
            }

            var name = ""

            val intent = Intent(Intent.ACTION_SEND)

            if (this::lastPostData.isInitialized) {
                name = lastPostData.fileName
                if (lastPostData.isVideo == "true") {
                    intent.type = "video"
                } else {
                    intent.type = "image/jpeg"
                }

            } else if (this::postData.isInitialized) {

                name = fileName
                if (postData["isVideo"] == "true") {
                    intent.type = "video"
                } else {
                    intent.type = "image/jpeg"
                }

            }

            val sd = requireContext().getExternalFilesDir("InstaReels")
            val toShare = File(sd, name)

            val authority = "com.weaponoid.instareels.provider"
            val contentUri = FileProvider.getUriForFile(requireContext(), authority, toShare)
            intent.putExtra(Intent.EXTRA_STREAM, contentUri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.setPackage("com.instagram.android")

            val chooser = Intent.createChooser(intent, "Share File")

            val resInfoList: List<ResolveInfo> = requireActivity().packageManager
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(
                    packageName,
                    contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            startActivity(chooser)

        }

        openPostButton.setOnClickListener {

            allReels = viewModel.getAllDocuments()
            if (allReels?.isNotEmpty()!!) {
                lastPostData = allReels!![0]
            }

            try {

                println(lastPostData.postUrl)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(lastPostData.postUrl))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }


        }


        lastPost.setOnClickListener {

            allReels = viewModel.getAllDocuments()
            if (allReels?.isNotEmpty()!!) {
                lastPostData = allReels!![0]
            }

            val intent = Intent(requireContext(), DetailView::class.java)
            intent.putExtra("isVideo", lastPostData.isVideo)
            intent.putExtra("fileUri", lastPostData.videoUri)

            requireContext().startActivity(intent)
        }


    }


}