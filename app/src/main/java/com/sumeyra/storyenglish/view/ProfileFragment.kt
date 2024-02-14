package com.sumeyra.storyenglish.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.sumeyra.storyenglish.adapter.FeedAdapter
import com.sumeyra.storyenglish.adapter.ProfileAdapter
import com.sumeyra.storyenglish.databinding.ProfileFragmentBinding
import com.sumeyra.storyenglish.model.Post
import com.sumeyra.storyenglish.model.Profile

class ProfileFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    private lateinit var adapter : ProfileAdapter
    val postProfileList = ArrayList<Profile>()



    private var _binding : ProfileFragmentBinding ?= null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileImage.setOnClickListener { selectPhoto(it) }
        getUser()
        getStoryFromFirebase()


        val layoutManager = LinearLayoutManager(requireContext())
        binding.profileRecyclerview.layoutManager = layoutManager
        adapter = ProfileAdapter(postProfileList)
        binding.profileRecyclerview.adapter = adapter



    }

    private fun selectPhoto(view: View) {
        //gallery gitme izin işlemleri vs
    }

    private fun getUser(){
        val currentUserEmail = auth.currentUser!!.email
        val currentUserName = auth.currentUser!!.displayName

        binding.profileUserEmailText.text = currentUserEmail
        binding.profileUserNameText.text = currentUserName
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getStoryFromFirebase() {
        db.collection("Posts").orderBy("date" , Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error != null) {
                val context: Context = requireContext()
                Toast.makeText(context , error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else {
                if (snapshot != null && !snapshot.isEmpty){
                    val documents = snapshot.documents

                    postProfileList.clear()

                    for (document in documents) {

                        if (document.get("userEmail") == auth.currentUser!!.email){
                            val storyHeader = document.get("storyHeader") as String
                            val story = document.get("story") as String
                            val word = document.get("words") as String

                            val downloadedPost = Profile( word, storyHeader,story)
                            postProfileList.add(downloadedPost)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

            }
        }
    }

}