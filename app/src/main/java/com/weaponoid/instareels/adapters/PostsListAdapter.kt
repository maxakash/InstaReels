package com.weaponoid.instareels.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weaponoid.instareels.R
import com.weaponoid.instareels.persistance.Document
import com.weaponoid.instareels.utils.loadListImage
import com.weaponoid.instareels.views.DetailView
import kotlinx.android.synthetic.main.item_view.view.*

class PostsListAdapter(private var postsList: MutableList<Document>) :
    RecyclerView.Adapter<PostsListAdapter.ViewHolder>() {


    fun updateUiList(newPostList: MutableList<Document>) {
        postsList.clear()
        postsList.addAll(newPostList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = postsList[position]
        holder.view.postImage.loadListImage(post.videoUri)
        holder.view.caption.text = post.caption
        holder.view.dp.loadListImage(post.dpUrl)
        holder.view.handle.text = post.handle


        if (post.isVideo == "false") {
            holder.view.isVideo.setImageDrawable(holder.view.context.getDrawable(R.drawable.image))

        }

        holder.view.postItem.setOnClickListener {

            val intent = Intent(holder.view.context, DetailView::class.java)
            intent.putExtra("isVideo", post.isVideo)
            intent.putExtra("fileUri", post.videoUri)
            intent.putExtra("postUrl", post.postUrl)
            intent.putExtra("hashtag", post.hashtag)
            intent.putExtra("caption", post.caption)
            intent.putExtra("fileName", post.fileName)
            intent.putExtra("id",post.documentId)

            holder.view.context.startActivity(intent)

        }

    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

    }
}