package com.example.colega.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.colega.api.RetrofitClient
import com.example.colega.data.source.Source
import com.example.colega.data.source.SourceRepository
import com.example.colega.db.MyDatabase
import com.example.colega.models.news.SourceResponse
import com.example.colega.models.news.SourceResponseItem
import com.example.colega.worker.LoadSourceWorker
import com.example.colega.worker.WorkerKeys
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

private const val TAG = "SourceViewModel"
class SourceViewModel(application: Application): AndroidViewModel(application) {

    private val allSources: MutableLiveData<List<SourceResponseItem>?> = MutableLiveData()
    private val repository: SourceRepository
    private val sourceWorkInfo: LiveData<List<WorkInfo>>
    private val workManager = WorkManager.getInstance(application)

    init {
        val sourceDao = MyDatabase.getDatabase(application).sourceDao()
        repository = SourceRepository(sourceDao)
        sourceWorkInfo = workManager.getWorkInfosByTagLiveData(WorkerKeys.TAG_SOURCE_DATA)
    }

    fun getSourceWorkInfo(): LiveData<List<WorkInfo>> = sourceWorkInfo

    fun fetchSource(userId: String){

        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder().putString(WorkerKeys.SOURCE_INPUT_DATA, userId).build()

        val periodicTimeWorkRequest = PeriodicWorkRequest.Builder(LoadSourceWorker::class.java, 2, TimeUnit.HOURS)
            .addTag(WorkerKeys.TAG_SOURCE_DATA)
            .setInputData(inputData)
            .setConstraints(constraint)
            .setInitialDelay(3L,TimeUnit.SECONDS)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            WorkerKeys.SYNC_SOURCE_DATA,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicTimeWorkRequest
        )

    }

    fun getAllSourceFromDB(): LiveData<List<Source>> = repository.getAllSources()


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

    fun insertSourceToDB(sources: List<Source>){
        viewModelScope.launch {
            repository.insertSource(sources)
        }
    }

    fun deleteAllSourceFromDB(){
        viewModelScope.launch {
            repository.deleteAllSource()
        }
    }

    fun updateSource(source: Source){
        viewModelScope.launch {
            repository.updateSource(source)
        }
    }
}