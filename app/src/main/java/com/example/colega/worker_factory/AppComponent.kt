package com.example.colega.worker_factory

import android.content.Context
import com.example.colega.NewsApplication
import com.example.colega.di.SingletonInstance
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

//@Singleton
//@Component(modules = [SingletonInstance::class])
interface AppComponent {

//    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun inject(application: NewsApplication)
}