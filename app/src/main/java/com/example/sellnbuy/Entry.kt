package com.example.sellnbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Entry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        supportActionBar?.hide()
        }
    fun login(view: View) {
        val intent= Intent(this, login::class.java)
        startActivity(intent)
    }

    fun signup(view: View) {
        val intent= Intent(this, sign_up::class.java)
        startActivity(intent)
    }
}