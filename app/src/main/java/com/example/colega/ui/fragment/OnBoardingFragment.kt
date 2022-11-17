package com.example.colega.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.colega.adapter.OnBoardingAdapter
import com.example.colega.databinding.FragmentOnBoardingBinding
import com.example.colega.dummy.DummyData
import com.example.colega.utils.Utils
import android.view.ViewGroup as ViewGroup1

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var sharedPref: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup1?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = OnBoardingAdapter(DummyData.onBoardingItems)
        binding.vpOnBoarding.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpOnBoarding.offscreenPageLimit = 3
        binding.vpOnBoarding.adapter = adapter
        binding.wormDot.attachTo(binding.vpOnBoarding)
        sharedPref = requireActivity().getSharedPreferences(Utils.name, Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.putBoolean(Utils.firstInstall, false)
        edit.apply()

    }
}