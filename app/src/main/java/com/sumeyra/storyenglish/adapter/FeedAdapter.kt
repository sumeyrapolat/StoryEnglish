package com.sumeyra.storyenglish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sumeyra.storyenglish.R
import com.sumeyra.storyenglish.databinding.FeedRowBinding
import com.sumeyra.storyenglish.model.Post

class FeedAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(val binding : FeedRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = FeedRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FeedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.binding.recyclerRowUsername.text = postList.get(position).userName
        holder.binding.recyclerViewWordText.text = postList.get(position).words
        holder.binding.recyclerViewStoryHeader.text = postList.get(position).storyHeader
        holder.binding.recyclerViewStoryText.text = postList.get(position).story
    }


}

