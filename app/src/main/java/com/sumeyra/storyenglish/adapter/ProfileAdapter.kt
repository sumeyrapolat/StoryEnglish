package com.sumeyra.storyenglish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sumeyra.storyenglish.databinding.ProfileRowBinding
import com.sumeyra.storyenglish.model.Post
import com.sumeyra.storyenglish.model.Profile

class ProfileAdapter(val profilePost: ArrayList<Profile>) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    class ProfileViewHolder( val binding: ProfileRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ProfileRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return profilePost.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.binding.profileStoryHeader.text = profilePost.get(position).storyHeader
        holder.binding.profileStoryText.text = profilePost.get(position).story
        holder.binding.profileWordText.text = profilePost.get(position).words
    }
}

