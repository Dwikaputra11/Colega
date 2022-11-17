package com.example.colega.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.colega.api.NewsService
import com.example.colega.api.UserService
import com.example.colega.di.SingletonInstance
import com.example.colega.data.source.Source
import com.example.colega.data.source.SourceDao
import com.example.colega.db.MyDatabase
import com.example.colega.models.news.SourceResponse
import com.example.colega.models.news.SourceResponseItem
import com.example.colega.models.user.UserFollowingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "LoadSourceWorker"
@HiltWorker
class LoadSourceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val sourceDao: SourceDao,
    val userService: UserService,
    val newsService: NewsService
): CoroutineWorker(context, workerParams) {

    private lateinit var userId: String
//    private val sourceDao = MyDatabase.getDatabase(applicationContext).sourceDao()

//    @Inject lateinit var sourceDao: SourceDao
//    @Inject lateinit var newsService: NewsService
//    @Inject lateinit var userService: UserService

    override suspend fun doWork(): Result {
        userId = inputData.getString(WorkerKeys.SOURCE_INPUT_DATA).toString()
        Log.d(TAG, "doWork: $userId")
        try {
            newsService.getAllSourceNews()
                .enqueue(object : Callback<SourceResponse>{
                    override fun onResponse(
                        call: Call<SourceResponse>,
                        response: Response<SourceResponse>
                    ) {
                        if(response.isSuccessful){
                            Log.d(TAG, "onResponse: Success")
                            response.body()?.let { sourceResponse ->
                                syncWithUserFollowingSource(sourceResponse.sources)
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
            Log.d(TAG, "doWork: Failed -> ${e.message}")
            return Result.failure()
        }
    }


    fun syncWithUserFollowingSource(list: List<SourceResponseItem>){
        userService.getUserFollowingSource(userId)
            .enqueue(object : Callback<List<UserFollowingSource>>{
                override fun onResponse(
                    call: Call<List<UserFollowingSource>>,
                    response: Response<List<UserFollowingSource>>
                ) {
                    if(response.isSuccessful){
                        Log.d(TAG, "onResponse: Response Success Sync Started")
                        response.body()?.let { followingSource ->
                            val source = list.map { source ->
                                Source(
                                    id = 0,
                                    userId = userId,
                                    name = source.name,
                                    url = source.url,
                                    description = source.description,
                                    country = source.country,
                                    language = source.language,
                                    category = source.category,
                                    sourceId = source.id,
                                    isFollow = followingSource.any { it.sourceId == source.id }
                                )
                            }.toList()
                            insertSourceToDB(source)
                        }
                    }else{
                        Log.d(TAG, "onResponse: Sync Response Unsuccessfully")
                        throw Exception()
                    }
                }

                override fun onFailure(call: Call<List<UserFollowingSource>>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    throw Exception()
                }

            })
    }

    fun insertSourceToDB(list: List<Source>){
        Log.d(TAG, "insertSourceToDB: Started")
        MyDatabase.databaseWriteExecutor.execute {
            sourceDao.deleteAllSource()
            sourceDao.postSource(list)
        }
        Log.d(TAG, "insertSourceToDB: Finished")
    }

}