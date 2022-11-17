package com.example.colega

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

private const val TAG = "NewsApplication"

@HiltAndroidApp
class NewsApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workConfiguration: Configuration

        override fun getWorkManagerConfiguration(): Configuration {
            Log.d(TAG, "getWorkManagerConfiguration: Started")
            return workConfiguration
        }

}