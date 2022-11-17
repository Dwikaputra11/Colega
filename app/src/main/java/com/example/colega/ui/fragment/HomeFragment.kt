package com.example.colega.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.colega.adapter.HomeAdapter
import com.example.colega.databinding.FragmentHomeBinding
import com.example.colega.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val userVM: UserViewModel by viewModels()
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
        var username = ""
        userVM.dataUser.observe(viewLifecycleOwner){
            username = it.username
        }
        binding.tvUsername.text = username
    }

}