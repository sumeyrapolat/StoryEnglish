package com.sumeyra.storyenglish.view

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
import androidx.fragment.app.Fragment
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

class feedFragment: Fragment() {

    private var _binding : FeedFragmentBinding ?= null
    private val binding get () =  requireNotNull(_binding)

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var adapter: FeedAdapter

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
            findNavController().navigate(R.id.action_feedFragment_to_signInFragment)

        }else if(item.itemId == R.id.story_upload){
            //upload fragment a geçilsin
            findNavController().navigate(R.id.action_feedFragment_to_uploadFragment)

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
        getDataFromFirebase()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.layoutManager = layoutManager
        adapter = FeedAdapter(postList)
        binding.feedRecyclerView.adapter = adapter


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFromFirebase(){
        // burada veri çekmek işlmeleri için ayır bir fonk yaz
        db.collection("Posts").orderBy("date" , Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error != null) {
                val context: Context = requireContext()
                Toast.makeText(context , error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else {
                if (snapshot != null && !snapshot.isEmpty){
                        val documents = snapshot.documents

                        postList.clear()

                        for (document in documents) {
                            val userName = document.get("userName") as String
                            val storyHeader = document.get("storyHeader") as String
                            val story = document.get("story") as String
                            val word = document.get("words") as String

                            val downloadedPost = Post(userName, word, storyHeader,story)
                            postList.add(downloadedPost)
                        }

                        adapter.notifyDataSetChanged()
                    }

            }
        }
    }






}