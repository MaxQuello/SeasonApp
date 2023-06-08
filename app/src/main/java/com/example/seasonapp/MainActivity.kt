package com.example.seasonapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.seasonapp.databinding.ActivityMainBinding

open class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dialog :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = findViewById(R.id.drawer_layout)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.bottomsheetlayout)

        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.TOP)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener() {
            drawerLayout.openDrawer(GravityCompat.START)
            true
        }


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeButton -> replaceFragment(HomeFragment())
                R.id.notifyButton -> replaceFragment(NotifyFragment())
                R.id.contactsButton -> replaceFragment(ContattiFragment())
                R.id.profileButton -> replaceFragment(ProfileFragment())
            }
            true
        }

        binding.fab.setOnClickListener{
            dialog.show()

        }

        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dialog.hide()
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_camere -> {
                    val fragment = CamereFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit()
                }
                R.id.nav_ristorante -> {
                    val fragment = RistoranteFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit()
                }
                R.id.nav_servizi -> {
                    val fragment = ServiziFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit()
                }
            }
            true
        }


    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit()
    }

}






