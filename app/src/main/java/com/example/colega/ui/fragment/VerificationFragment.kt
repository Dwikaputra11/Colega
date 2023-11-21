package com.example.colega.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.colega.R
import com.example.colega.adapter.VerificationAdapter
import com.example.colega.databinding.FragmentVerificationBinding
import com.google.android.material.tabs.TabLayoutMediator

class VerificationFragment : DialogFragment() {

    private lateinit var binding : FragmentVerificationBinding
    private lateinit var onPageChangeListener   : ViewPager2.OnPageChangeCallback
    private lateinit var adapter                : VerificationAdapter
    private lateinit var listener: OnSuccessVerification
    private lateinit var chiperFragment: ChiperFragment
    private lateinit var steganographyFragment: SteganographyFragment

    interface OnSuccessVerification{
        fun onSuccess()
    }

    fun setOnSuccessVerification(listener: OnSuccessVerification){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chiperFragment = ChiperFragment()
        steganographyFragment = SteganographyFragment()
        adapter = VerificationAdapter(requireActivity())
        configViews()

        chiperFragment.setOnSuccessVerify(object : ChiperFragment.OnSuccessVerify{
            override fun onSuccess() {
                listener.onSuccess()
                dismiss()
            }
        })

        steganographyFragment.setOnSuccessVerify(object : SteganographyFragment.OnSuccessVerify{
            override fun onSuccess() {
                listener.onSuccess()
                dismiss()
            }
        })
    }

    private fun configViews() {
        onPageChangeListener = object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        }

        adapter.addFragment(
            chiperFragment,
            "Caesar Chiper"
        )
        adapter.addFragment(
            steganographyFragment,
            "Gambar"
        )

        requireDialog().window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        binding.tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.blue_light
            )
        )

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = adapter.getTabTitle(pos)
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(onPageChangeListener)
    }
}