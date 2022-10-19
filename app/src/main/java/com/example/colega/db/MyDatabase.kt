package com.example.colega.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.colega.data.article.HeadlineDao
import com.example.colega.data.article.HeadlineNews
import com.example.colega.data.article.RelatedNews
import com.example.colega.data.article.RelatedNewsDao
import com.example.colega.data.source.Source
import com.example.colega.data.source.SourceDao
import com.example.colega.data.users.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(
    entities = [User::class, Bookmark::class, FollowingSource::class, HeadlineNews::class, RelatedNews::class, Source::class],
    version = 4,
    autoMigrations = [
        AutoMigration (from = 1, to = 2, spec = MyDatabase.MyAutoMigration::class),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ],
)
abstract class MyDatabase: RoomDatabase() {
    @DeleteColumn(tableName = "Bookmark", columnName = "isCheck")
    class MyAutoMigration: AutoMigrationSpec
    abstract fun userDao(): UserDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun followingDao(): FollowingSourceDao
    abstract fun relatedNews(): RelatedNewsDao
    abstract fun headlineDao(): HeadlineDao
    abstract fun sourceDao(): SourceDao
    companion object {
        private const val NUMBER_OF_THREADS = 4

        @Volatile
        private var INSTANCE: MyDatabase? = null
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
                    "colega_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}