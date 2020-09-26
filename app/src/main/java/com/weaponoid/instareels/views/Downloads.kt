package com.weaponoid.instareels.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.weaponoid.instareels.R
import com.weaponoid.instareels.adapters.PostsListAdapter
import com.weaponoid.instareels.persistance.Document
import com.weaponoid.instareels.viewmodels.DownloadsViewModel
import kotlinx.android.synthetic.main.downloads_fragment.*

class Downloads : Fragment() {


    private lateinit var viewModel: DownloadsViewModel
    private lateinit var listAdapter: PostsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.downloads_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DownloadsViewModel::class.java)
        viewModel.init(requireActivity())
        val mutableList = mutableListOf<Document>()
        listAdapter = PostsListAdapter(mutableList)

        downloadPosts.apply {
            adapter = listAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        }


        viewModel.getAllDocuments()?.observe(viewLifecycleOwner, {
            listAdapter.updateUiList(it)

            if(it.isEmpty()){
                downloadPosts.visibility = View.GONE
                noDownloadedPost.visibility = View.VISIBLE
            }else{
                noDownloadedPost.visibility = View.GONE
                downloadPosts.visibility = View.VISIBLE
            }
        })

    }

}