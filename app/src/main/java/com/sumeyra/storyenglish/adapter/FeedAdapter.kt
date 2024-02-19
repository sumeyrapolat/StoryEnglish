package com.sumeyra.storyenglish.adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.sumeyra.storyenglish.R
import com.sumeyra.storyenglish.databinding.FeedRowBinding
import com.sumeyra.storyenglish.model.Post
import com.sumeyra.storyenglish.view.AnotherUserFragment

class FeedAdapter(private val context: Context, val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    private lateinit var auth : FirebaseAuth
    inner class FeedViewHolder(val binding : FeedRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            auth = Firebase.auth
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = FeedRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FeedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val post = postList[position]

        holder.binding.recyclerRowUsername.text = post.userName
        holder.binding.recyclerViewWordText.text = post.words
        holder.binding.recyclerViewStoryHeader.text = post.storyHeader
        holder.binding.recyclerViewStoryText.text = post.story

        Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.binding.userImageView)

        holder.binding.recyclerRowUsername.setOnClickListener {view->

            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_feedFragment_to_anotherUser)

        }
    }

}

