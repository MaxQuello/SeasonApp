package com.example.seasonapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.seasonapp.databinding.ActivityMainBinding

open class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = binding.toolbar2
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener() {
            drawerLayout.openDrawer(GravityCompat.START)
            true
        }


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> HomeFragment()
                R.id.notifyFragment -> NotifyFragment()
                R.id.contattiFragment -> NotifyFragment()
                R.id.profileFragment -> ProfileFragment()
            }
            true
        }


        binding.fab.setOnClickListener{
            showBottomDialog() }

    }
    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)

        cancelButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.TOP)
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

}






