package com.weaponoid.instareels.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weaponoid.instareels.R
import java.io.File

class PostsListAdapter(val postsList: ArrayList<File>) :
    RecyclerView.Adapter<PostsListAdapter.viewHolder>() {


    fun updateUiList(newUiList: ArrayList<File>) {
        postsList.clear()
        postsList.addAll(newUiList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
//        val file = fileList[position]
//        holder.view.imageListImage.loadListImage(Uri.parse(file.absolutePath).toString())
//        val fileName = File(file.absolutePath).name
//        if(fileName.endsWith(".jpg") || fileName.endsWith(".gif")){
//            holder.view.playVideo.visibility = View.GONE
//        }
//        holder.view.imageListImage.setOnClickListener {
//            val intent = Intent(holder.view.context, ImagesDetail::class.java)
//            intent.putExtra("imagePath", file.absolutePath)
//            holder.view.context.startActivity(intent)
//
//        }
    }

    class viewHolder(var view: View) : RecyclerView.ViewHolder(view) {

    }
}