package com.example.colega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.example.colega.databinding.ActivitySourceBinding

class SourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySourceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.sourceToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}