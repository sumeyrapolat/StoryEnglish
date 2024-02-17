package com.sumeyra.storyenglish.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sumeyra.storyenglish.databinding.FeedRowBinding
import com.sumeyra.storyenglish.model.Post

class FeedAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(val binding : FeedRowBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.recyclerRowUsername.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Tıklanan öğenin pozisyonunu alabilir ve istediğiniz işlemleri yapabilirsiniz
                val clickedPost = postList[position]
                // Tıklanan öğe ile ilgili işlemleri yapabilirsiniz
                // Örneğin: Tıklanan öğenin detaylarına gitme gibi
                // clickedPost.userName, clickedPost.words, vb. şeklinde öğelere erişebilirsiniz

                //burada başka bir fragmente gitmek istiyorum
            }
        }
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



    }

}

