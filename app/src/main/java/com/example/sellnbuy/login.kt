package com.example.sellnbuy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.Userr
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val email = findViewById<EditText>(R.id.email1)
        val password = findViewById<EditText>(R.id.password2)
        var pb=findViewById<ProgressBar>(R.id.pbL)
        pb.visibility= View.GONE


    }
    fun userLoggedInSuccess(user: Userr) {
        Log.i("Username : ",user.Username)
        Log.i("email : ",user.email)
    }
    private fun validateDetails(): Boolean {
        val email = findViewById<EditText>(R.id.email1)
        val password = findViewById<EditText>(R.id.password2)
        var pb=findViewById<ProgressBar>(R.id.pbL)

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
        var pb=findViewById<ProgressBar>(R.id.pbL)

        if(validateDetails())
        {

            pb.visibility= View.VISIBLE
            val emailL=email.text.toString().trim{it <=' '}
            val passwordL=password.text.toString().trim{it <=' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailL,passwordL)
                .addOnCompleteListener {task ->
                        if(task.isSuccessful)
                        {
                            firestore().getUserDetails(this)
                            pb.visibility= View.GONE
                            val intent=Intent(this,profile::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            pb.visibility= View.GONE
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

    fun godirectprofile(user:Userr)
    {
        if(user.profileCompleted==0)
        {
            val intent=Intent(this@login,profile::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this,"Please fill your details first",Toast.LENGTH_LONG).show()
        }
        else
        {
            val intent=Intent(this@login,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


