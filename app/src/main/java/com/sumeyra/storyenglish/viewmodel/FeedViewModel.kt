package com.sumeyra.storyenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sumeyra.storyenglish.model.Email
import com.sumeyra.storyenglish.model.Post
class FeedViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _postList = MutableLiveData<List<Post>>()
    val postList: LiveData<List<Post>> get() = _postList
    private lateinit var email: String


    fun getAllStories() {
        db.collection("Feed").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _postList.value = emptyList()
                } else {
                    if (snapshot != null && !snapshot.isEmpty) {
                        val documents = snapshot.documents
                        val posts = mutableListOf<Post>()
                        for (document in documents) {
                            val userName = document.get("userName") as String
                            val storyHeader = document.get("storyHeader") as String
                            val story = document.get("story") as String
                            val word = document.get("words") as String
                            val imageUrl = document.get("imageUrl") as String?
                            val userEmail = document.get("userEmail") as String

                            val downloadedPost =
                                Post(userName, word, storyHeader, story, imageUrl, userEmail)
                            posts.add(downloadedPost)
                        }

                        _postList.value = posts
                    } else {
                        _postList.value = emptyList()
                    }
                }
            }
    }


}