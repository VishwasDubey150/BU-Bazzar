package com.example.sellnbuy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.User
import com.google.firebase.auth.FirebaseAuth

class login : baseActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
    }
    fun userLoggedInSuccess(user: User) {
        Log.i("Username : ",user.Username)
        Log.i("email : ",user.email)

        if(user.profileCompleted==0)
        {
            val intent=Intent(this@login,profile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
            finish()
            Toast.makeText(this,"please complete your profile",Toast.LENGTH_SHORT).show()
        }
        if (user.profileCompleted==1)
        {
            val intent=Intent(this@login,dashboard::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
            finish()
        }
    }
    private fun validateDetails(): Boolean {
        val email = findViewById<EditText>(R.id.email1)
        val password = findViewById<EditText>(R.id.password2)

        return when {

            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@login, "Please enter email", Toast.LENGTH_SHORT).show()
                false

            }
            TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@login, "Please enter password", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loggingin()
    {
        val email = findViewById<EditText>(R.id.email1)
        val password = findViewById<EditText>(R.id.password2)

        if(validateDetails())
        {
            showPB()
            val emailL=email.text.toString().trim{it <=' '}
            val passwordL=password.text.toString().trim{it <=' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailL,passwordL)
                .addOnCompleteListener {task ->
                        if(task.isSuccessful)
                        {
                            hidePB()
                            firestore().getUserDetails(this)
                        }
                        else
                        {
                            hidePB()
                            Toast.makeText(this@login,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()

                        }
                    }
        }

        }
        fun gt_signin(view: View) {
        val intent = Intent(this, sign_up::class.java)
        startActivity(intent)
        }

    fun loginn(view: View) {
        loggingin()
    }

    fun forgot(view: View) {
        val intent = Intent(this, Forgot_pass::class.java)
        startActivity(intent)
    }

    fun godirectprofile(user:User)
    {
        if (user.profileCompleted == 0) {
            val intent = Intent(this@login, profile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@login, MainActivity::class.java))
        }
        finish()
    }
    fun backk(view: View) {
        startActivity(Intent(this@login,Entry::class.java))
    }
}

