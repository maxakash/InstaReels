package com.weaponoid.instareels.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.weaponoid.instareels.persistance.Document
import com.weaponoid.instareels.persistance.DocumentDao
import com.weaponoid.instareels.persistance.DocumentDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DownloadsViewModel : ViewModel() {

    private lateinit var dao: DocumentDao
    private lateinit var executorService: ExecutorService


    fun init(context: Context) {
        dao = DocumentDatabase.getInstance(context).documentDao()
        executorService = Executors.newSingleThreadExecutor()
    }

    fun getAllDocuments(): LiveData<MutableList<Document>>? {
        return dao.all
    }



}