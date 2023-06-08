package com.example.seasonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.seasonapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

open class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        binding.fab.setOnClickListener {
          /*  val fragment = binding.homeLayout.findFragment<Fragment>()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            transaction.replace()*/
        }

        val toolbar = binding.toolbar2
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener() {
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.notifyFragment,
                R.id.contattiFragment,
                R.id.profileFragment
            )
        )
        bottomNavigationView.setupWithNavController(navController)

    }

        /*
            super.setSupportActionBar(toolbar)

                val toggle = ActionBarDrawerToggle(
                this, drawerLayout, /*toolbar,*/ R.string.drawer_open, R.string.drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, HomeFragment())
                    .commit()
                val menu = navigationView.menu
                menu.findItem(R.id.homeFragment)?.isChecked = true
            }
        */
        public fun onNavigationItemSelected(item: MenuItem): Boolean {
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
            }
        }
    }




