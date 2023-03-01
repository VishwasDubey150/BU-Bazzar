package com.example.sellnbuy

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.webkit.MimeTypeMap.getSingleton
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.MimeTypeFilter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import io.grpc.Context.Storage
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var mselected:Uri?=null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val btn = findViewById<Button>(R.id.btn)
        val select = findViewById<Button>(R.id.select)
        val upload = findViewById<Button>(R.id.Upload)
        var selected_image = findViewById<ImageView>(R.id.image)
        btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, login::class.java))
            finish()
        }

        upload.setOnClickListener {
            if(mselected!=null)
            {

                val imageExtension=MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(mselected!!))
            }
        }



        select.setOnClickListener {
            var selected_image = findViewById<ImageView>(R.id.image)
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
            {
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(galleryIntent,222)

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    121
                )
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 121)
        {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent,222)
        }
        else { Toast.makeText(this, "You Denied", Toast.LENGTH_LONG).show()
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var selected_image = findViewById<ImageView>(R.id.image)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 222) {
                if (data != null) {
                    try {
                        val mselected = data.data!!
                        Glide.with(this).load(mselected).into(selected_image)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}