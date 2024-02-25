package com.sumeyra.storyenglish.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sumeyra.storyenglish.view.UploadFragmentDirections

class UploadViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun uploadPost(words: String, storyHeader: String, story: String, imageUrl: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val userName = auth.currentUser!!.displayName.toString()
        val date = Timestamp.now()
        val email = auth.currentUser?.email ?: return onFailure.invoke("User not authenticated")

        val postMap = hashMapOf<String, Any>(
            "words" to words,
            "storyHeader" to storyHeader,
            "story" to story,
            "userName" to userName,
            "date" to date,
            "userEmail" to email,
            "imageUrl" to imageUrl
        )

        db.collection("Posts").document(email).collection("userPostInfo").add(postMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess.invoke()
            }
        }.addOnFailureListener { error ->
            onFailure.invoke(error.localizedMessage ?: "Unknown error occurred")
        }

        db.collection("Feed").add(postMap).addOnCompleteListener { task->
            if (task.isSuccessful){
                onSuccess.invoke()
            }
        }.addOnFailureListener { error->
            onFailure.invoke(error.localizedMessage ?: "Unknown error occurred")

        }
    }

}