package com.example.colega.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.colega.data.Bookmark
import com.example.colega.data.BookmarkDao
import com.example.colega.data.BookmarkRepository
import com.example.colega.db.MyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application): AndroidViewModel(application) {
    private val repository: BookmarkRepository

    init {
        val bookmarkDao = MyDatabase.getDatabase(application)
            .bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
    }

    fun getAllBookmark(userId: Int): LiveData<List<Bookmark>> = repository.getAllBookmark(userId)

    fun insertBookmark(bookmark: Bookmark){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBookmark(bookmark)
        }
    }

    fun updateBookmark(bookmark: Bookmark){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBookmark(bookmark)
        }
    }

    fun deleteAllBookmark(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllBookmark()
        }
    }

    fun deleteBookmark(bookmark: Bookmark){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteBookmark(bookmark)
        }
    }
}