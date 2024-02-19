package com.sumeyra.storyenglish.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.sumeyra.storyenglish.adapter.AnotherUserAdapter
import com.sumeyra.storyenglish.databinding.AnotherUserFragmentBinding
import com.sumeyra.storyenglish.model.Profile

class AnotherUserFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    private lateinit var adapter : AnotherUserAdapter
    val postProfileList = ArrayList<Profile>()

    private var _binding : AnotherUserFragmentBinding ?= null
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

    private fun getImage(){
    }
}