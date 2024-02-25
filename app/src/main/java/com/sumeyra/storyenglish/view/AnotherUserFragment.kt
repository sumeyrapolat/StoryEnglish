package com.sumeyra.storyenglish.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.sumeyra.storyenglish.adapter.AnotherUserAdapter
import com.sumeyra.storyenglish.databinding.AnotherUserFragmentBinding
import com.sumeyra.storyenglish.model.AnotherUser

class AnotherUserFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    private lateinit var adapter : AnotherUserAdapter

    private var _binding : AnotherUserFragmentBinding ?= null
    private var userEmail: String? = null
    val postUserList = ArrayList<AnotherUser>()




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

        _binding = AnotherUserFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserInfo()

        if (userEmail !=null ){
            getPosts()
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.anotherUserProfileRecyclerview.layoutManager = layoutManager
        adapter =AnotherUserAdapter(postUserList)
        binding.anotherUserProfileRecyclerview.adapter = adapter



    }

    private fun getUserInfo(){
        //recyclerview içindeki kısım haricinde
        val userImage = arguments?.getString("userImage") ?: ""
        Glide.with(binding.anotherUserProfile).load(userImage).into(binding.anotherUserProfile)

        val userName = arguments?.getString("userName") ?: ""
        if (userName == auth.currentUser!!.displayName){
            binding.anotherUserProfileUserNameText.text = userName
            /*val action = AnotherUserFragmentDirections.actionAnotherUserFragmentToProfileFragment()
            findNavController().navigate(action)*/

        }else{
            binding.anotherUserProfileUserNameText.text = userName
        }
        //Burada neden email i alamadığıma sonradan bak
        // xml de de kontrol et
       userEmail = arguments?.getString("userEmail") ?: ""
       binding.anotherUserEmailText.text = userEmail

       userEmail = arguments?.getString("userEmail") ?: ""


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPosts() {
        if (userEmail != null && !userEmail.isNullOrEmpty()){
            val email = userEmail!!
            db.collection("Posts")
                .document(email).collection("userPostInfo")
                .orderBy("date" , Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null ){
                        Toast.makeText(requireContext() , error.localizedMessage, Toast.LENGTH_SHORT).show()
                    }else{
                        if (snapshot !=null && !snapshot.isEmpty){
                            val documents = snapshot.documents
                             for (document in documents){
                                if (document.get("userEmail") == email){
                                    val userName = document.get("userName") as String
                                    val userEmail = document.get("userEmail") as String
                                    val storyHeader = document.get("storyHeader") as String
                                    val story = document.get("story") as String
                                    val word = document.get("words") as String

                                    println(userName)
                                    println(userEmail)
                                    println(storyHeader)
                                    println(story)
                                    println(word)


                                    val downloadedPost = AnotherUser(userName, word,storyHeader,story,userEmail)
                                    postUserList.add(downloadedPost)
                                }
                            }
                            adapter.notifyDataSetChanged()

                        }
                    }
                }
        }

    }
}