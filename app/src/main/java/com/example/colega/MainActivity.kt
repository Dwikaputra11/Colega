package com.example.colega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * TODO:
 * 1. Sync database with api Work Manager (every 1 hour)
 * 2. Check application is connected to internet or not
 * 3. Add Pagination to recycle view
 * 4. Add Following fragment with article from source that user follow
 * 5. Implement search from database to search article
 * 6. Implement drawer to source list page and add profile logout option in there
 * */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}