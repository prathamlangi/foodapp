package com.example.foodapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fragement.FAQsFragment
import fragement.FavouritesFragment
import fragement.HomeFragment
import fragement.OrderFragment
import fragement.ProfileFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorlayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerlayout)
        coordinatorlayout = findViewById(R.id.coordinatorlayout)
        toolbar = findViewById(R.id.my_toolbar)
        frameLayout = findViewById(R.id.framelayout)
        navigationView = findViewById(R.id.navigationlayout)
        var previousMenuItem: MenuItem? = null

        setToolBar()
        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isCheckable = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.menu_home -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }

                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout,ProfileFragment())
                        .commit()
                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.menu_favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, FavouritesFragment())
                        .commit()
                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }

                R.id.menu_order -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, OrderFragment())
                        .commit()
                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()
                }

                R.id.menu_faqs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, FAQsFragment())
                        .commit()
                    supportActionBar?.title = "FAQs"
                    drawerLayout.closeDrawers()
                }

                R.id.menu_logout -> {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes") { _, _ ->
                            val sharedPreferences=getSharedPreferences("The Preference",Context.MODE_PRIVATE)
                            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
                                sharedPreferences.edit().remove("user_id").apply()
                                sharedPreferences.edit().remove("name").apply()
                                sharedPreferences.edit().remove("email").apply()
                                sharedPreferences.edit().remove("mobile_number").apply()
                                sharedPreferences.edit().remove("address").apply()

                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                           dialog.dismiss()
                            openHome()
                            drawerLayout.closeDrawers()
                        }
                        val dialog=builder.create()
                        dialog.show()
                }
            }
            return@setNavigationItemSelectedListener (true)
        }

    }

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout, fragment)
        transaction.commit()
        navigationView.setCheckedItem(R.id.menu_home)
        supportActionBar?.title = "Home"
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.framelayout)
        when (frag) {
            !is HomeFragment -> {
                openHome()
            }
            else -> {
                super.onBackPressed()
            }

        }
    }
}