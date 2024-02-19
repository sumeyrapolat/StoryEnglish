package com.sumeyra.storyenglish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.sumeyra.storyenglish.databinding.AnotherUserRowBinding
import com.sumeyra.storyenglish.model.Post
import com.sumeyra.storyenglish.model.Profile

class AnotherUserAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<AnotherUserAdapter.AnotherViewHolder>() {
    class AnotherViewHolder( val binding: AnotherUserRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotherViewHolder {
        val binding = AnotherUserRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AnotherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: AnotherViewHolder, position: Int) {
        holder.binding.anotherUserProfileStoryHeader.text = postList.get(position).storyHeader
        holder.binding.anotherUserProfileStoryText.text = postList.get(position).story
        holder.binding.anotherUserProfileWordText.text = postList.get(position).words
    }
}