package com.weaponoid.instareels.viewmodels

import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.weaponoid.instareels.persistance.DocumentDao
import com.weaponoid.instareels.persistance.DocumentDatabase
import com.weaponoid.instareels.utils.successToast
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailViewViewModel : ViewModel() {

    private lateinit var dao: DocumentDao
    private lateinit var executorService: ExecutorService

    fun init(context: Context) {
        dao = DocumentDatabase.getInstance(context).documentDao()
        executorService = Executors.newSingleThreadExecutor()
    }


    fun deleteDocument(docId: Int, fileName: String, context: Context) {
        val docs = dao.findAll()

        for (doc in docs) {

            if (doc.documentId == docId) {
                executorService.execute { dao.delete(doc) }
            }
        }

        val file = File(context.getExternalFilesDir("InstaReels"), fileName)
        if (file.exists()) {
            file.delete()
        }
        context.successToast("Deleted")


    }

    fun share(name: String, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        val sd = context.getExternalFilesDir("InstaReels")
        val toShare = File(sd, name)

        println(toShare.exists())
        val authority = "com.weaponoid.instareels.provider"
        val contentUri = FileProvider.getUriForFile(context, authority, toShare)
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)

        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, "Share File")

        val resInfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                contentUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        context.startActivity(chooser)
    }


    fun copyHashtag(hashtag: String, context: Context) {
        val clipboardManager =
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)!!
        val clip = ClipData.newPlainText("hashtag", hashtag)
        clipboardManager.setPrimaryClip(clip)

        context.successToast("Hashtags copied to clipboard.")
    }

    fun copyCaption(caption: String, context: Context) {
        val clipboardManager =
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)!!
        val clip = ClipData.newPlainText("caption", caption)
        clipboardManager.setPrimaryClip(clip)

        context.successToast("Hashtags copied to clipboard.")
    }


    fun rePost(name: String, context: Context, isVideo: String) {

        val intent = Intent(Intent.ACTION_SEND)


        if (isVideo == "true") {
            intent.type = "video"
        } else {
            intent.type = "image/jpeg"
        }


        val sd = context.getExternalFilesDir("InstaReels")
        val toShare = File(sd, name)

        val authority = "com.weaponoid.instareels.provider"
        val contentUri = FileProvider.getUriForFile(context, authority, toShare)
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.setPackage("com.instagram.android")

        val chooser = Intent.createChooser(intent, "Share File")

        val resInfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                contentUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        context.startActivity(chooser)
    }

    fun openPost(url: String, context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}