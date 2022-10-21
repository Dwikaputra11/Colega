package com.example.colega.data.source

import androidx.lifecycle.LiveData
import com.example.colega.db.MyDatabase
import javax.inject.Inject

class SourceRepository @Inject constructor(private val sourceDao: SourceDao) {

    fun getAllSources(): LiveData<List<Source>> = sourceDao.getAllSource()

    fun isSourceEmpty(): Int = sourceDao.countSourceSize()

    fun insertSource(sources: List<Source>){
        MyDatabase.databaseWriteExecutor.execute {
            sourceDao.postSource(sources)
        }
    }

    fun deleteAllSource(){
        MyDatabase.databaseWriteExecutor.execute {
            sourceDao.deleteAllSource()
        }
    }

    fun updateSource(source: Source){
        MyDatabase.databaseWriteExecutor.execute {
            sourceDao.updateSource(source)
        }
    }
}