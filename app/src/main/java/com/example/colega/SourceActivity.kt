package com.example.colega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colega.adapter.SourceNewsAdapter
import com.example.colega.data.FollowingSource
import com.example.colega.databinding.ActivitySourceBinding
import com.example.colega.models.news.SourceResponseItem
import com.example.colega.viewmodel.FollowingSourceViewModel
import com.example.colega.viewmodel.SourceViewModel
import com.example.colega.viewmodel.UserViewModel

private const val TAG = "SourceActivity"

class SourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySourceBinding
    private lateinit var sourceVM:SourceViewModel
    private lateinit var followingSourceVM: FollowingSourceViewModel
    private lateinit var userVm: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sourceVM = ViewModelProvider(this)[SourceViewModel::class.java]
        followingSourceVM = ViewModelProvider(this)[FollowingSourceViewModel::class.java]
        userVm = ViewModelProvider(this)[UserViewModel::class.java]

        var userId = ""
        userVm.dataUser.observe(this){
            userId = it.userId.toString()
            Log.d(TAG, "onCreate: UserId: $userId")
        }

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
            override fun onItemClick(sourceResponseItem: SourceResponseItem, isClicked: Boolean) {
                if(isClicked){
                    val followingSource = FollowingSource(name = sourceResponseItem.name,  userId = userId, sourceId = sourceResponseItem.id)
                    followingSourceVM.postFollowingSourceToApi(userId, followingSource)
                    followingSourceVM.getPostFollowingSource().observe(this@SourceActivity){
                        if(it  != null) Toast.makeText(this@SourceActivity, "Add to Following", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(this@SourceActivity, "Failed to Post", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    followingSourceVM.getSingleSourceFromApi(userId, sourceResponseItem.id)
                    followingSourceVM.getSingleSource().observe(this@SourceActivity){ user ->
                        if(user != null) {
                            followingSourceVM.deleteFollowingSource(userId, user[0].id)
                            followingSourceVM.getDeleteFollowingSource().observe(this@SourceActivity){
                                if(it  != null) Toast.makeText(this@SourceActivity, "Delete from Following", Toast.LENGTH_SHORT).show()
                                else Toast.makeText(this@SourceActivity, "Failed to Delete", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
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