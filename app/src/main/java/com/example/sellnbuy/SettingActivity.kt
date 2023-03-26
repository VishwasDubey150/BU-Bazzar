package com.example.sellnbuy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : baseActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        var lcard = findViewById<CardView>(R.id.logout_card)
        lcard.visibility = View.GONE
        supportActionBar?.hide()
    }

    fun logout(view: View) {
        var lcard = findViewById<CardView>(R.id.logout_card)
        lcard.visibility = View.VISIBLE
    }

    fun no_logout(view: View) {
        var lcard = findViewById<CardView>(R.id.logout_card)
        lcard.visibility = View.GONE
    }

    fun yes_logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, login::class.java))
        Toast.makeText(this, "You are logged out!", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun updateprofile(view: View) {
        startActivity(Intent(this@SettingActivity,profile::class.java))
        finish()
    }
}