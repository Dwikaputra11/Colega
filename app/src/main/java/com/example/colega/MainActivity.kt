package com.example.colega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.AndroidEntryPoint

/**
 * TODO:
 * 1. Sync database with api Work Manager (every 1 hour) SUCCESS
 * 2. Check application is connected to internet or not NOT YET
 * 3. Add Pagination to recycle view NOT YET
 * 4. Add Following fragment with article from source that user follow SUCCESS
 * 5. Implement search from database to search article NOT YET
 * 6. Implement drawer to source list page and add profile logout option in there SUCCESS
 * */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}