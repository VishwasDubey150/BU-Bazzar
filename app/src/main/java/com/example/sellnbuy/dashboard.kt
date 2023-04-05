package com.example.sellnbuy

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sellnbuy.databinding.ActivityDashboardBinding

class dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_Order, R.id.navigation_dashboard, R.id.navigation_Product,R.id.navigation_sold
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        var card=findViewById<CardView>(R.id.quit_card)
        card.visibility=View.GONE
    }

    override fun onBackPressed() {
        var card=findViewById<CardView>(R.id.quit_card)
        card.visibility=View.VISIBLE
    }

    fun no_quit(view: View) {
        var card=findViewById<CardView>(R.id.quit_card)
        card.visibility=View.GONE
    }
    fun yes_quit(view: View) {
        finishAffinity()
    }
}