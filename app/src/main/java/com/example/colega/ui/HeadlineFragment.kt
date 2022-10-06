package com.example.colega.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.colega.R
import com.example.colega.databinding.FragmentHeadlineBinding
import com.example.colega.databinding.HeadlineItemBinding


class HeadlineFragment : Fragment() {
    private val TAG = "HeadlineFragment"
    private lateinit var binding: FragmentHeadlineBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadlineBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}