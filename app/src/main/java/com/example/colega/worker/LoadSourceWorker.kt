package com.example.colega.worker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.colega.api.RetrofitClient
import com.example.colega.data.source.Source
import com.example.colega.db.MyDatabase
import com.example.colega.models.news.SourceResponse
import com.example.colega.models.news.SourceResponseItem
import com.example.colega.viewmodel.SourceViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "LoadSourceWorker"
@Suppress("BlockingMethodInNonBlockingContext")
class LoadSourceWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    private val application = applicationContext
    private val sourceDao = MyDatabase.getDatabase(application).sourceDao()

    override suspend fun doWork(): Result {
        try {
            RetrofitClient.instanceFilm.getAllSourceNews()
                .enqueue(object : Callback<SourceResponse>{
                    override fun onResponse(
                        call: Call<SourceResponse>,
                        response: Response<SourceResponse>
                    ) {
                        if(response.isSuccessful){
                            Log.d(TAG, "onResponse: Success")
                            response.body()?.let { sourceResponse ->
                                insertSourceToDB(sourceResponse.sources)
                            }
                        }else{
                            Log.d(TAG, "onResponse: Unsuccessfully")
                            throw Exception()
                        }
                    }

                    override fun onFailure(call: Call<SourceResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: Failed to get data source")
                        throw Exception()
                    }

                })
            Log.d(TAG, "doWork: Success")
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }


    fun insertSourceToDB(list: List<SourceResponseItem>){
        Log.d(TAG, "insertSourceToDB: Started")
        val sources = list.map {
            Source(
                id = 0,
                name = it.name,
                url = it.name,
                description = it.description,
                country = it.country,
                language = it.language,
                category = it.category
            )
        }.toList()
        MyDatabase.databaseWriteExecutor.execute {
            sourceDao.postSource(sources)
        }
        Log.d(TAG, "insertSourceToDB: Finished")
    }

}