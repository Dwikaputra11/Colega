package com.example.colega.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.colega.R
import com.example.colega.adapter.HomeAdapter
import com.example.colega.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = HomeAdapter(requireActivity())
        adapter.addFragment(FeedsFragment(), "Feeds")
        adapter.addFragment(HeadlineFragment(), "Headline")
        adapter.addFragment(FollowingFragment(), "Following")

        binding.homePager.adapter = adapter
        binding.homePager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.homePager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}