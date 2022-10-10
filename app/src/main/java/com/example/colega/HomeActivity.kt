package com.example.colega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colega.databinding.ActivityHomeBinding
import com.example.colega.ui.HomeFragment
import com.example.colega.ui.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, HomeFragment())
            .commit()

        binding.btmNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bookmark -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, BookmarkFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, HomeFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, ProfileFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    false
                }
            }
        }
    }
}