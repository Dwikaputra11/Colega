package com.example.colega

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.colega.databinding.ActivityHomeBinding
import com.example.colega.ui.BookmarkFragment
import com.example.colega.ui.HomeFragment
import com.example.colega.ui.ProfileFragment


private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureToolbar()
        configureNavigationDrawer()

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, HomeFragment())
            .commit()

        binding.btmNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bookmark -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, BookmarkFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, HomeFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, ProfileFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.empty,menu)
        return true
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = binding.toolbarLayout.toolbar
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_round_menu_24)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureNavigationDrawer(){
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.source -> {
                    startActivity(Intent(this, SourceActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home ->{
                binding.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}