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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.sumeyra.storyenglish.R
import com.sumeyra.storyenglish.databinding.LogInFragmentBinding

class logInFragment : Fragment() {

    private var _binding : LogInFragmentBinding ?= null
    private val binding get() = requireNotNull(_binding)

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LogInFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logInButton.setOnClickListener { logIn(it) }
    }

    private fun logIn(view: View){
        //firebase e kayıt ekranı

        val email = binding.loginUserEmailText.text.toString()
        val password = binding.loginPasswordText.text.toString()

        if (password != "" && email != ""){

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    //kullanıcı ile ilgilli kaydetmek istediim verileri alıyorum
                    val currentUser = auth.currentUser?.displayName.toString()
                    val context: Context = requireContext()

                    Toast.makeText(context, "Welcome ${currentUser}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_logInFragment_to_feedFragment)
                }
            }.addOnFailureListener {error->
                val context: Context = requireContext()
                Toast.makeText(context,error.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

    }
}