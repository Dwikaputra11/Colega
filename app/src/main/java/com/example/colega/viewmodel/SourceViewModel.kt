package com.example.colega.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.colega.api.RetrofitClient
import com.example.colega.models.news.SourceResponse
import com.example.colega.models.news.SourceResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SourceViewModel"
class SourceViewModel(application: Application): AndroidViewModel(application) {

    private val allSources: MutableLiveData<List<SourceResponseItem>?> = MutableLiveData()

    fun getAllSources(): MutableLiveData<List<SourceResponseItem>?> = allSources

    fun getSourcesFromApi(){
        RetrofitClient.instanceFilm.getAllSourceNews()
            .enqueue(object : Callback<SourceResponse>{
                override fun onResponse(
                    call: Call<SourceResponse>,
                    response: Response<SourceResponse>
                ) {
                    if(response.isSuccessful){
                        if(response.body() != null){
                            allSources.postValue(response.body()!!.sources)
                            Log.d(TAG, "onResponse: ${response.body()!!.sources.size}")
                        }else{
                            Log.d(TAG, "onResponse: Unsuccessfully")
                            allSources.postValue(null)
                        }
                    }
                }

                override fun onFailure(call: Call<SourceResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    allSources.postValue(null)
                }

            })
    }
}