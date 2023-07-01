package com.example.seasonapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.databinding.ActivityMainBinding
import com.example.seasonapp.profilazione.LoginFragment


open class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dialog: Dialog
    private lateinit var navController: NavController
    private lateinit var db: DbManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = findViewById(R.id.drawer_layout)
        db = DbManager(this).open()
//Comportamento Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

//Comportamento BottomNavigation
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeButton -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_homeFragment2)
                R.id.notifyButton -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_notifyFragment)
                R.id.contactsButton -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_contattiFragment)
                R.id.profileButton -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_profileFragment)
            }
            true
        }
//Comportamento Drawer
        binding.navigationView.setNavigationItemSelectedListener {item ->
            when (item.itemId) {
                R.id.nav_camere -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_camereFragment)
                R.id.nav_ristorante -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_ristoranteFragment)
                R.id.nav_servizi -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_serviziFragment)
                R.id.nav_recensioni -> findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_recensioniFragment)
                R.id.nav_logout -> logout()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

//Comportamento floatingActionButton
        dialog = Dialog(this)
        dialog.setContentView(R.layout.bottomsheetlayout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.TOP)

        binding.fab.setOnClickListener {
            dialog.show()

        }

        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dialog.hide()
        }

//Comportamento Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


    }

    private fun logout() {
        // Ottenere un'istanza delle SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Ottenere un'istanza dell'editor delle SharedPreferences
        val editor = sharedPreferences.edit()

        // Impostare lo stato del login su false
        editor.putBoolean("isLoggedIn", false)

        // Applicare le modifiche
        editor.apply()

        Log.i("PROVA","HO FATTO IL LOGOUT")

        db.deleteAll()
        Toast.makeText(this, "Hai effettuato il logout", Toast.LENGTH_LONG).show()

    }


}