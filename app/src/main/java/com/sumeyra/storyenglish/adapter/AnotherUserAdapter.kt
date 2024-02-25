package com.sumeyra.storyenglish.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sumeyra.storyenglish.databinding.AnotherUserRowBinding
import com.sumeyra.storyenglish.model.AnotherUser
class AnotherUserAdapter(val anotherUserList: ArrayList<AnotherUser>) : RecyclerView.Adapter<AnotherUserAdapter.AnotherViewHolder>() {
    class AnotherViewHolder( val binding: AnotherUserRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotherViewHolder {
        val binding = AnotherUserRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AnotherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return anotherUserList.size
    }

    override fun onBindViewHolder(holder: AnotherViewHolder, position: Int) {

        holder.binding.anotherUserProfileStoryHeader.text = anotherUserList.get(position).storyHeader
        holder.binding.anotherUserProfileStoryText.text = anotherUserList.get(position).story
        holder.binding.anotherUserProfileWordText.text = anotherUserList.get(position).words
    }



}