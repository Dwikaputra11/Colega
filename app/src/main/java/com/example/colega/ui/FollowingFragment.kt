package com.example.colega.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.colega.R
import com.example.colega.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private val TAG = "FollowingFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}