package com.sumeyra.storyenglish.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sumeyra.storyenglish.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {

    private var _binding : ProfileFragmentBinding ?= null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        getUser()
        binding.profileImage.setOnClickListener { selectPhoto(it) }
    }

    private fun selectPhoto(view: View) {
        //gallery gitme izin işlemleri vs
    }

    private fun getUser(){

    }
}