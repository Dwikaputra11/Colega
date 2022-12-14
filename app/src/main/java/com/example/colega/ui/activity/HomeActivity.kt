package com.example.colega.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.colega.R
import com.example.colega.databinding.ActivityHomeBinding
import com.example.colega.ui.fragment.BookmarkFragment
import com.example.colega.ui.fragment.HomeFragment
import com.example.colega.ui.fragment.ProfileFragment
import com.example.colega.viewmodel.SourceViewModel
import com.example.colega.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "HomeActivity"

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val userVM: UserViewModel by viewModels()
    private val sourceVM: SourceViewModel by viewModels()
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
                R.id.logout ->{
                    logoutDialog()
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

    private fun logoutDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            run {
                userVM.clearUserPref()
                sourceVM.deleteAllSourceFromDB()
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            }
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.logout_account))
        builder.setMessage(getString(R.string.confirm_logout))
        builder.create().show()
    }
}