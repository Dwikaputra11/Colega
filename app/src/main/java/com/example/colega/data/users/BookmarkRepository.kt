package com.example.colega.data.users

import androidx.lifecycle.LiveData
import com.example.colega.db.MyDatabase

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    fun getAllBookmark(userId: Int) : LiveData<List<Bookmark>> = bookmarkDao.getAllBookmark(userId)

    fun insertBookmark(bookmark: Bookmark){
        MyDatabase.databaseWriteExecutor.execute{
            bookmarkDao.insertBookmark(bookmark)
        }
    }

    fun updateBookmark(bookmark: Bookmark){
        MyDatabase.databaseWriteExecutor.execute {
            bookmarkDao.updateBookmark(bookmark)
        }
    }

    fun deleteAllBookmark(){
        MyDatabase.databaseWriteExecutor.execute {
            bookmarkDao.deleteAll()
        }
    }

    fun deleteBookmark(bookmark: Bookmark){
        MyDatabase.databaseWriteExecutor.execute {
            bookmarkDao.deleteBookmark(bookmark)
        }
    }

}