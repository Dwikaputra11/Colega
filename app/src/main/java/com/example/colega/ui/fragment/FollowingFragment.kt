package com.example.colega.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colega.adapter.FollowingAdapter
import com.example.colega.databinding.FragmentFollowingBinding
import com.example.colega.models.user.UserFollowingSource
import com.example.colega.viewmodel.FollowingSourceViewModel
import com.example.colega.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FollowingFragment"

@AndroidEntryPoint
class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private val userFollowingSourceVM: FollowingSourceViewModel by viewModels()
    private val userVM: UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: Started")
        userVM.dataUser.observe(viewLifecycleOwner){
            setViews(it.userId.toString())
        }
    }

    private fun setViews(userId: String){
        userVM.dataUser.observe(viewLifecycleOwner){
            if(it != null){
                val adapter = FollowingAdapter()
                userFollowingSourceVM.getFollowingFromApi(userId)
                userFollowingSourceVM.getFollowingSource().observe(viewLifecycleOwner){ list ->
                    if(list != null){
                        binding.tvEmptyFollow.visibility = View.GONE
                        binding.rvFollowing.visibility = View.VISIBLE
                        adapter.setFollowingList(list)
                        binding.rvFollowing.adapter = adapter
                        binding.rvFollowing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                }

                adapter.setOnItemClickListener(object : FollowingAdapter.OnItemClickListener{
                    override fun onItemClick(source: UserFollowingSource) {
                        Log.d(TAG, "onItemClick: $source")
                    }
                })
            }
        }
    }


}