package com.example.colega.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.colega.data.Bookmark
import com.example.colega.data.BookmarkDao
import com.example.colega.data.User
import com.example.colega.data.UserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [User::class, Bookmark::class], version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookmarkDao(): BookmarkDao
    companion object {
        private const val NUMBER_OF_THREADS = 4

        @Volatile
        private var INSTANCE: com.example.colega.db.MyDatabase? = null
        val databaseWriteExecutor: ExecutorService =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS) // using for creating a database but not in main thread
//     that can interrupt our ui, so we have to create database in background

        //     that can interrupt our ui, so we have to create database in background
        fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(MyDatabase::class.java) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "notice_me_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}