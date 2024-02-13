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
import com.google.firebase.auth.userProfileChangeRequest
import com.sumeyra.storyenglish.R
import com.sumeyra.storyenglish.databinding.SignInFragmentBinding
import kotlin.math.sign

class signInFragment : Fragment() {

    private var _binding : SignInFragmentBinding ?= null
    private val binding get() =  requireNotNull(_binding)

    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth


        //eğer güncel kullanıcı varsa direkt bu ekranı atla feed aktivity ekranına gel
        val currentUser= auth.currentUser
        if (currentUser != null){
            findNavController().navigate(R.id.action_signInFragment_to_feedFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInButton.setOnClickListener { signIn(it) }
        binding.goToLogin.setOnClickListener { goToLogIn(it) }
    }

    private fun signIn(view: View) {
        //firebase kayıt işlemleri
        val email = binding.userEmailText.text.toString()
        val password = binding.passwordText.text.toString()
        val userName = binding.userNameText.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if (task.isSuccessful){

                //username update
                val currentUser = auth.currentUser
                val updateUser = userProfileChangeRequest {
                    displayName = userName
                }

                if (currentUser != null){
                    currentUser.updateProfile(updateUser).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val context: Context = requireContext()
                            //Toast.makeText(context, "Profile username added.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Toast.makeText(context, "Welcome ${userName}", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signInFragment_to_feedFragment)

            }
        }.addOnFailureListener{error->
            val context: Context = requireContext()
            Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()

        }
    }

    private fun goToLogIn(view: View) {
        //login page e git
        findNavController().navigate(R.id.action_signInFragment_to_logInFragment)
    }
}