package com.whileloop.instareels.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.whileloop.instareels.R
import java.io.File

class ImageListAdapter(val fileList: ArrayList<File>) :
    RecyclerView.Adapter<ImageListAdapter.viewHolder>() {


    fun updateUiList(newUiList: ArrayList<File>) {
        fileList.clear()
        fileList.addAll(newUiList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount() = fileList.size

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