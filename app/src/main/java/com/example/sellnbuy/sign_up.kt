package com.example.sellnbuy
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.sellnbuy.R.id
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class sign_up : baseActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val username=findViewById<EditText>(id.un1)
        val contact=findViewById<EditText>(id.mobileno)
        val email=findViewById<EditText>(id.email2)
        val password=findViewById<EditText>(id.password3)
        val signbtn=findViewById<Button>(id.signbtn)
        val checkbox=findViewById<CheckBox>(R.id.checkbox)
        supportActionBar?.hide()
        signbtn.setOnClickListener {
            when{
                TextUtils.isEmpty(email.text.toString().trim{it<=' '})->{
                    showErrorSnackBar("Please Enter your email", true)
                    false
                }
                TextUtils.isEmpty(password.text.toString().trim{it<=' '})->{
                    showErrorSnackBar("Please Enter your password", true)
                    false
                }
                TextUtils.isEmpty(username.text.toString().trim{it<=' '})->{
                    showErrorSnackBar("Please Enter your Username", true)
                    false
                }

                TextUtils.isEmpty(contact.text.toString().trim{it<=' '})->{
                    showErrorSnackBar("Please Enter your contact number.", true)
                    false
                }
                !checkbox.isChecked -> {
                    showErrorSnackBar("Please check the T&C box", true)
                    false
                }

                else  ->{
                    val emailO: String=email.text.toString().trim(){it <=' '}
                    val passwordO: String=password.text.toString().trim(){it <=' '}
                    showPB()
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailO,passwordO)
                        .addOnCompleteListener (
                                OnCompleteListener<AuthResult>{
                                    task ->
                                    if(task.isSuccessful)
                                    {
                                        val firebaseUser:FirebaseUser=task.result!!.user!!
                                        val user= User(
                                            firebaseUser.uid,
                                            username.text.toString().trim{it <=' '},
                                            email.text.toString().trim{it <=' '},
                                            contact.text.toString().trim(){it<=' '}
                                        )
                                        firestore().registerUser(this,user)
                                        hidePB()

                                        val intent=Intent(this@sign_up,login::class.java)
                                        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id",firebaseUser.uid)
                                        intent.putExtra("email_id",emailO)

                                        startActivity(intent)
                                        finish()
                                    }
                                    else
                                    {
                                        hidePB()
                                        Toast.makeText(this@sign_up,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                                    }
                                })
                            }
                }
            }
        }


    fun login(view: View) {
        val intent = Intent(this, login::class.java)
        startActivity(intent)
    }

    fun back(view: View) {
        val intent = Intent(this, Entry::class.java)
        startActivity(intent)
    }

    fun userRegistrationSuccess()
    {
        hidePB()
        Toast.makeText(this,"Account created successfully",Toast.LENGTH_SHORT).show()
    }

}

