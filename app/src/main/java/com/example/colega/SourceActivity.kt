package com.example.colega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.example.colega.adapter.SourceNewsAdapter
import com.example.colega.data.source.Source
import com.example.colega.models.user.DataFollowingSource
import com.example.colega.databinding.ActivitySourceBinding
import com.example.colega.models.news.SourceResponseItem
import com.example.colega.viewmodel.FollowingSourceViewModel
import com.example.colega.viewmodel.SourceViewModel
import com.example.colega.viewmodel.UserViewModel
import com.google.android.material.shape.ShapePath.PathQuadOperation
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SourceActivity"

@AndroidEntryPoint
class SourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySourceBinding
    private lateinit var sourceVM:SourceViewModel
    private lateinit var followingSourceVM: FollowingSourceViewModel
    private lateinit var userVm: UserViewModel
    private lateinit var adapter: SourceNewsAdapter
    private var userId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sourceVM = ViewModelProvider(this)[SourceViewModel::class.java]
        followingSourceVM = ViewModelProvider(this)[FollowingSourceViewModel::class.java]
        userVm = ViewModelProvider(this)[UserViewModel::class.java]

        userVm.dataUser.observe(this){
            if(it != null){
                requestSource(it.userId.toString())
            }
            Log.d(TAG, "onCreate: UserId: $userId")
        }

        adapter = SourceNewsAdapter()
        binding.rvSourceNews.adapter = adapter
        binding.rvSourceNews.layoutManager =  object : LinearLayoutManager(binding.root.context){
            override fun canScrollVertically(): Boolean {
                return true
            }
        }

        // TODO: FIX BUTTON WHEN CLICK FOLLOW
        adapter.setOnItemClickListener(object : SourceNewsAdapter.OnItemClickListener{
            override fun onItemClick(source: Source) {
                // source follow is true it will post to userApi
                if(!source.isFollow){
                    postToApi(source)
                }else{
                    deleteFromApi(source)
                }
                // no matter what state, source always be update when button clicked
                updateDatabase(source)
            }
        })

        setSupportActionBar(binding.sourceToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun requestSource(userId: String) {
        sourceVM.fetchSource(userId)
        sourceVM.getSourceWorkInfo().observe(this){
            val workInfo = it[0]
            if(workInfo.state.isFinished || workInfo.state == WorkInfo.State.ENQUEUED){
                sourceVM.getAllSourceFromDB().observe(this){ list ->
                    binding.shimmerLayout.startShimmer()
                    adapter.setSourceNewsList(list)
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvSourceNews.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateDatabase(source: Source) {
        // update button state if following or follow
        val updateSource = Source(
            name = source.name,
            userId = source.userId,
            sourceId = source.sourceId,
            language = source.language,
            country = source.country,
            description = source.description,
            isFollow = !source.isFollow,
            category = source.category,
            id = source.id,
            url = source.url
        )
        sourceVM.updateSource(updateSource)
    }

    private fun postToApi(source: Source){
        // insert to API if source is following
        // api body
        val dataFollowingSource = DataFollowingSource(
            name = source.name,
            userId = source.userId,
            sourceId = source.sourceId,
            language = source.language,
            country = source.country,
            description = source.description
        )
        followingSourceVM.postFollowingSourceToApi(source.userId, dataFollowingSource)
        followingSourceVM.getPostFollowingSource().observe(this@SourceActivity){
            if(it  != null) Toast.makeText(this@SourceActivity, "Add to Following", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this@SourceActivity, "Failed to Post", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFromApi(source: Source){
        // delete the source from api if the button change to follow base on userId and sourceId in source data
        followingSourceVM.getSingleSourceFromApi(source.userId, source.sourceId)
        followingSourceVM.getSingleSource().observe(this){
            followingSourceVM.deleteFollowingFromApi(it.userId, it.id)
            followingSourceVM.getDeleteFollowingSource().observe(this@SourceActivity){
                if(it  != null) Toast.makeText(this@SourceActivity, "Delete from Following", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this@SourceActivity, "Failed to Delete", Toast.LENGTH_SHORT).show()
            }
        }
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