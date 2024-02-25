package com.sumeyra.storyenglish.view


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
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
import com.sumeyra.storyenglish.viewmodel.UploadViewModel

class UploadFragment : Fragment(){
    private var _binding : UploadFragmentBinding ?= null
    private val binding get() =  requireNotNull(_binding)

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    private val uploadViewModel: UploadViewModel by viewModels()


    private var imageUrl: String? = null

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

        getImageUrl()



    }

    private fun getImageUrl(){
        val imageReference = storage.reference
        val uid = auth.currentUser!!.uid
        val imageName = "profileImage.jpg"
        val pathReference = imageReference.child("Images").child("UserPhoto").child(uid).child(imageName)

        pathReference.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()

            val words = binding.wordsText.text.toString()
            val storyHeader = binding.storyHeader.text.toString()
            val story = binding.storyText.text.toString()


            uploadViewModel.uploadPost(words, storyHeader, story, imageUrl, {
                // Başarılı yükleme durumunda yapılacaklar
                goToFeed()

            }, { errorMessage ->
                // Hata durumunda yapılacaklar
                println(errorMessage)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            })
           goToFeed()

        }.addOnFailureListener { error ->
            val errorMessage = error.localizedMessage ?: "Unknown error occurred"
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

    }

    fun goToFeed(){
        binding.shareButton.setOnClickListener { view->
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_uploadFragment_to_feedFragment)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}