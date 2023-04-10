package com.example.sellnbuy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.sellnbuy.firestore.firestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User

class SettingActivity : baseActivity() {

    private lateinit var mUserDetails :User

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
        val intent=Intent(this,login::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(this, "You are logged out!", Toast.LENGTH_SHORT).show()
        finish()
    }

//    private fun getUserDetails()
//    {
//        showPB()
//        firestore().getUserDetails(this)
//    }
//
//    fun userDetailsSuccess(user:com.example.sellnbuy.model.User)
//    {
//        var image=findViewById<ImageView>(R.id.iv_profile)
//        var name=findViewById<TextView>(R.id.name2)
//        var email=findViewById<TextView>(R.id.email2)
//        var mobile=findViewById<TextView>(R.id.mobile2)
//        var address=findViewById<TextView>(R.id.address2)
//        var gender=findViewById<TextView>(R.id.gender2)
//        hidePB()
//        GlideLoader(this@SettingActivity).loadUserPicture(user.image,image)
//        name.text="${user.Username}"
//        email.text="${user.email}"
//        mobile.text="${user.mobile}"
//        address.text="${user.address}"
//        gender.text="${user.gender}"
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        getUserDetails()
//    }

    fun b_dashboard(view: View) {
        val intent=Intent(this@SettingActivity,dashboard::class.java)
        startActivity(intent)
    }
}