package com.sumeyra.storyenglish.view

import androidx.fragment.app.Fragment
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.sumeyra.storyenglish.R
import com.sumeyra.storyenglish.adapter.FeedAdapter
import com.sumeyra.storyenglish.databinding.FeedFragmentBinding
import com.sumeyra.storyenglish.model.Post
import com.sumeyra.storyenglish.viewmodel.FeedViewModel
import java.util.zip.ZipEntry

class FeedFragment : Fragment(){
    private var _binding : FeedFragmentBinding ?= null
    private val binding get () =  requireNotNull(_binding)

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var adapter: FeedAdapter

    private val feedViewModel : FeedViewModel by viewModels()

    var postList = ArrayList<Post>()

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuInflater = inflater
        menuInflater.inflate(R.menu.feed_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out){
            //kullanıcı çıkışı yapılsın
            auth.signOut()
            // sign in ekranına dönülsün
                val action = FeedFragmentDirections.actionFeedFragmentToSignInFragment()
                findNavController().navigate(action)

        }else if(item.itemId == R.id.story_upload){
            //upload fragment a geçilsin
            val action = FeedFragmentDirections.actionFeedFragmentToUploadFragment()
            findNavController().navigate(action)

        }else if(item.itemId == R.id.profile){
            val action = FeedFragmentDirections.actionFeedFragmentToProfileFragment()
            findNavController().navigate(action)
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Bu satır menüyü etkinleştirir
        auth = Firebase.auth
        db = Firebase.firestore

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FeedFragmentBinding.inflate(inflater,container,false)
        val view = binding.root

        return view


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = FeedAdapter(requireContext(), ArrayList())
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.adapter = adapter

        feedViewModel.postList.observe(viewLifecycleOwner, Observer { posts ->
            adapter.updateData(posts)
        })


        // ViewModel'den verileri al ve Firebase'den çek
        feedViewModel.getAllStories()

    }




}