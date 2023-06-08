package com.example.seasonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

open class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
/*
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        //var toolbar: Toolbar = findViewById(R.id.toolbar)
        //fun setSupportActionBar(toolbar: androidx.appcompat.widget.Toolbar?) {
        //    super.setSupportActionBar(toolbar)

                val toggle = ActionBarDrawerToggle(
                this, drawerLayout, /*toolbar,*/ R.string.drawer_open, R.string.drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            /if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, HomeFragment())
                    .commit()
                val menu = navigationView.menu
                menu.findItem(R.id.homeFragment)?.isChecked = true
            }
        }

            fun onNavigationItemSelected(item: MenuItem): Boolean {
            val fragment: Fragment = when (item.itemId) {
                R.id.camereFragment -> CamereFragment()
                R.id.ristoranteFragment -> RistoranteFragment()
                R.id.serviziFragment -> ServiziFragment()
                else -> throw IllegalArgumentException("Invalid menu item ID")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()

            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }

            override fun onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }                                                             */
        }
    }




