package com.weaponoid.instareels.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weaponoid.instareels.R
import com.weaponoid.instareels.views.DownloadsViewModel

class Downloads : Fragment() {


    private lateinit var viewModel: DownloadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.downloads_fragment, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(DownloadsViewModel::class.java)
    }

}