package com.example.colega.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.colega.R
import com.example.colega.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.flow.combine

class SplashScreenFragment : Fragment() {
    private var progressMax = 450
    private val TAG = "SplashScreenFragment"
    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressBar.max = progressMax
        for(i in 1..(progressMax - 5)){
            Handler(Looper.getMainLooper()).postDelayed({
                if(i in 101..150) {
                    binding.progressBar.progress = i
                }else if(i in 90..100){
                    binding.progressBar.progress = i + 2
                }else{
                    binding.progressBar.progress = i + 5
                }
                // error cause progress value over 300 is called more than 1 so navigation called more than 1
                if(binding.progressBar.progress >= progressMax){
                    Log.d(TAG, "onViewCreated: over 300")
                    Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                }
                Log.d(TAG, "onViewCreated: ${binding.progressBar.progress}")
            }, i.toLong())
        }
    }

}