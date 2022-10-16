package com.example.colega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colega.adapter.SourceNewsAdapter
import com.example.colega.databinding.ActivitySourceBinding
import com.example.colega.models.news.SourceResponseItem
import com.example.colega.viewmodel.SourceViewModel

private const val TAG = "SourceActivity"

class SourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySourceBinding
    private lateinit var sourceVM:SourceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sourceVM = ViewModelProvider(this)[SourceViewModel::class.java]

        sourceVM.getSourcesFromApi()
        val adapter = SourceNewsAdapter()
        binding.rvSourceNews.adapter = adapter
        binding.rvSourceNews.layoutManager =  object : LinearLayoutManager(binding.root.context){
            override fun canScrollVertically(): Boolean {
                return true
            }
        }
        sourceVM.getAllSources().observe(this){
            if(it != null){
                Log.d(TAG, "onCreate: ${it.size}")
                Log.d(TAG, "onCreate: $it")
                adapter.setSourceNewsList(it)
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.rvSourceNews.visibility = View.VISIBLE
            }
        }

        adapter.setOnItemClickListener(object : SourceNewsAdapter.OnItemClickListener{
            override fun onItemClick(sourceResponseItem: SourceResponseItem) {
                Log.d(TAG, "onItemClick: $sourceResponseItem")
            }
        })

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

    override fun onStart() {
        binding.shimmerLayout.startShimmer()
        super.onStart()
    }

    override fun onPause() {
        binding.shimmerLayout.stopShimmer()
        super.onPause()
    }
}