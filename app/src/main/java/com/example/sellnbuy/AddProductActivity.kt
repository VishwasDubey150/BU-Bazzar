package com.example.sellnbuy

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.sellnbuy.R
import java.io.IOException

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    fun upload_image(view: View) {

        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            showImageChooser(this)
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                profile.READ_STORAGE_PERMISSION_CODE)
        }
    }

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == profile.READ_STORAGE_PERMISSION_CODE) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showImageChooser(this)

        } else {
            Toast.makeText(
                this, "You Denied",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val iv_profile_user_image = findViewById<ImageView>(R.id.add_img)
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
        if (requestCode == profile.PICK_IMAGE_REQUEST_CODE) {
            if (data != null) {
                try {
                    var mselectedImageFileUri = data.data!!
                    Glide
                        .with(this)
                        .load(mselectedImageFileUri)
                        .centerCrop()
                        .placeholder(R.drawable.add)
                        .into(iv_profile_user_image)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


fun showImageChooser(activity: Activity) {
    val galleryIntent = Intent(
        Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    )
    startActivityForResult(galleryIntent, profile.PICK_IMAGE_REQUEST_CODE)
}

    fun submit(view: View) {}
}