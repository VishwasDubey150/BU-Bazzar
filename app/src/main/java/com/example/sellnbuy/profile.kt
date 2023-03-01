package com.example.sellnbuy

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sellnbuy.model.Userr
import com.example.sellnbuy.firestore.firestore
import java.io.IOException

class profile : AppCompatActivity() {
    private lateinit var muserdetails:Userr
    private var mselectedImageFileUri: Uri?=null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()
        var pbp=findViewById<ProgressBar>(R.id.pbp)
        pbp.visibility= View.GONE



    }
    fun back(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun submit(view: View) {
        var pbp=findViewById<ProgressBar>(R.id.pbp)
        var name=findViewById<EditText>(R.id.name)
        var email=findViewById<EditText>(R.id.uemail)
        var address=findViewById<EditText>(R.id.address)
        var contact=findViewById<EditText>(R.id.contactno)

        firestore().uploadImageToCloudStorage(this,mselectedImageFileUri)
        when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(name.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(address.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(contact.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter Contact no.", Toast.LENGTH_SHORT).show()
            }
            else->
            {
                pbp.visibility= View.VISIBLE
                val userHashMap=HashMap<String,Any>()
                val mobileno=contact.text.toString().trim{it <=' '}
                if(mobileno.isNotEmpty())
                {
                    userHashMap[Constants.MOBILE]=mobileno.toLong()
                }

                val name=name.text.toString().trim{it <=' '}
                if(name.isNotEmpty())
                {
                    userHashMap[Constants.USERNAME]=name.toString()
                }

                val address=address.text.toString().trim{it<=' '}
                if(address.isNotEmpty())
                {
                    userHashMap[Constants.ADDRESS]=address.toString()
                }
                pbp.visibility= View.GONE
                userProfileUpdateSuccess()
            }
        }
    }
    fun profile_picture(view: View) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Constants.showImageChooser(this)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this, "You Denied",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val dp=findViewById<ImageView>(R.id.dp)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mselectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(mselectedImageFileUri!!,dp)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    fun userProfileUpdateSuccess() {

        Toast.makeText(
            this,"profile Successfully updated",
            Toast.LENGTH_SHORT
        ).show()


        startActivity(Intent(this, dashboard::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String)
    {
        Toast.makeText(this,"Image Successfully Updated!!",Toast.LENGTH_SHORT).show()
    }
}
