package com.example.sellnbuy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.sellnbuy.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
//NOt WOrking
class Forgot_pass : baseActivity() {
    lateinit var binding: ActivityForgotPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotPassBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN

        binding.submit.setOnClickListener {
            resetpassword()
        }
    }
    fun back(view: View) {
        startActivity(Intent(this@Forgot_pass, login::class.java))
    }

    fun resetpassword()
    {
        val email: String = binding.email.text.toString().trim { it <= ' ' }
        if(email.isEmpty())
        {
            Toast.makeText(this,"Please enter email address", Toast.LENGTH_SHORT).show()
        }
        else
        {
            showPB()
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener{
                        task ->
                    if (task.isSuccessful)
                    {
                        hidePB()
                        Toast.makeText(this,"Email sent to reset Password", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else
                    {
                        hidePB()
                        Toast.makeText(this,task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}