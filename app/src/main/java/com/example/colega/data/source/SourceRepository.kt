package com.example.colega.data.source

import androidx.lifecycle.LiveData
import com.example.colega.db.MyDatabase

class SourceRepository(private val sourceDao: SourceDao) {

    fun getAllSources(): LiveData<List<Source>> = sourceDao.getAllSource()

    fun insertSource(source: Source){
        MyDatabase.databaseWriteExecutor.execute {
            sourceDao.postSource(source)
        }
    }

    fun deleteAllSource(){
        MyDatabase.databaseWriteExecutor.execute {
            sourceDao.deleteAllSource()
        }
    }
}