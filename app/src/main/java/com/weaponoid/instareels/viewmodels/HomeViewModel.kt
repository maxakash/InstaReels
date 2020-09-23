package com.weaponoid.instareels.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.weaponoid.instareels.persistance.Document
import com.weaponoid.instareels.persistance.DocumentDao
import com.weaponoid.instareels.persistance.DocumentDatabase
import org.jsoup.Jsoup
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HomeViewModel : ViewModel() {

    private lateinit var dao: DocumentDao
    private lateinit var executorService: ExecutorService


    fun init(context: Context) {
        dao = DocumentDatabase.getInstance(context).documentDao()
        executorService = Executors.newSingleThreadExecutor()
    }


    fun validateUrl(url: String) =
        (url.startsWith("https://") || url.startsWith("http://")) &&
                (url.contains("instagram.com/p/") || url.contains("instagram.com/tv/") || url.contains(
                    "instagram.com/reel/"
                ))


    fun getMediaUrl(url: String): HashMap<String, String> {
        val userAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
        val page = Jsoup.connect(url).userAgent(userAgent).get()


        //println(page.toString())

        var username = ""

        if (url.contains("instagram.com/p/")) {

            username = page.toString()
                .substringBefore("\",\"blocked_by_viewer\"")
                .substringAfter("\"username\":\"")

            var temp = ""
            while (username != "" && temp != username) {
                temp = username
                username = username
                    .substringBefore("\",\"blocked_by_viewer\"")
                    .substringAfter("\"username\":\"")

            }

        } else if (url.contains("instagram.com/reel/")) {

            username = page.toString()
                .substringAfter("@type\":\"ProfilePage\",\"@id\":\"https:\\/\\/www.instagram.com\\/")
                .substringBefore("\\/\"")

        }


        val title = page.select("meta[property=og:title]").first().attr("content")
        val thumbnail = page.select("meta[property=og:image]").first().attr("content")
        val st = title.split(" on Instagram: ")

        var caption = ""
        caption = if (st.size > 1) {
            st[1]
        } else {
            ""
        }

        var hashtags = ""
        for (element in page.select("meta[property=video:tag]")) {
            hashtags += " #${element.attr("content")}"
        }


        val shareUrl = page.select("link[rel=alternate]").first().attr("href")

        val data = hashMapOf<String, String>()

        when (page.select("meta[name=medium]").first().attr("content")) {
            "video" -> {
                data["mediaUrl"] = page.select("meta[property=og:video]").first().attr("content")
                data["isVideo"] = "true"
            }
            "image" -> {
                data["mediaUrl"] = page.select("meta[property=og:image]").first().attr("content")
                data["isVideo"] = "false"
            }
            else ->
                data["mediaUrl"] = ""

        }

        data["caption"] = caption
        data["hashtag"] = hashtags
        data["shareUrl"] = shareUrl
        data["handle"] = username
        data["thumbnail"] = thumbnail

        return data
    }


    fun getDP(url: String): String {


        println(url)
        val userAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"

        val page = Jsoup.connect(url).userAgent(userAgent).ignoreContentType(true).get()
        var dp = page.toString().substringAfter("\"profile_pic_url\":\"").substringBefore("\",\"")

        dp = dp.replace("amp;", "")
        println(dp)

        return dp

    }


    fun getAllDocuments(): List<Document>? {
        return dao.findAll()
    }


    fun saveDocument(document: Document?) {
        executorService.execute { dao.save(document) }
    }


}