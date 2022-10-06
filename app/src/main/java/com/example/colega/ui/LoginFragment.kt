package com.example.colega.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.colega.HomeActivity
import com.example.colega.R
import com.example.colega.databinding.FragmentLoginBinding
import com.example.colega.databinding.FragmentRegisterBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.clLayout.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
        }
        binding.btnSignUp.setOnClickListener {
            val registerFragment = RegisterFragment()
            registerFragment.show(requireActivity().supportFragmentManager, registerFragment.tag)
        }
    }

}