package com.sumeyra.storyenglish.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.sumeyra.storyenglish.R
import com.sumeyra.storyenglish.databinding.UploadFragmentBinding

class uploadFragment : Fragment() {

    private var _binding : UploadFragmentBinding ?= null
    private val binding get() =  requireNotNull(_binding)

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

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
        _binding = UploadFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shareButton.setOnClickListener { shareStory(it)}
    }

    private fun shareStory(view: View){

        //firestore a kaydetme istediğim verilerim

        val words = binding.wordsText.text.toString()
        val storyHeader = binding.storyHeader.text.toString()
        val story = binding.storyText.text.toString()
        val userName = auth.currentUser!!.displayName.toString()
        val date = Timestamp.now()

        // for profile
        val email = auth.currentUser!!.email.toString()



        //paylaşacağım değerleri bir hashmap içinde tutuyorum
        val postMap = hashMapOf<String, Any>()

        postMap.put("words", words)
        postMap.put("storyHeader", storyHeader)
        postMap.put("story", story)
        postMap.put("userName", userName)
        postMap.put("date", date)
        postMap.put("userEmail",email)


        db.collection("Posts").add(postMap).addOnCompleteListener { task->
            if (task.isSuccessful){
                //burada feed ekranına yönlendirme yapılabilir
                findNavController().navigate(R.id.action_uploadFragment_to_feedFragment)
            }
        }.addOnFailureListener { error->
            val context: Context = requireContext()
            Toast.makeText(context, error.localizedMessage,Toast.LENGTH_SHORT).show()

        }


    }


}