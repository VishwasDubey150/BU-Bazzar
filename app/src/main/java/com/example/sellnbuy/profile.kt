package com.example.sellnbuy

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sellnbuy.Constants.showImageChooser
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.User

class profile : baseActivity() {
    @SuppressLint("RestrictedApi")
    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mselectedImageFileUri: Uri? = null
    private lateinit var mUserDetail: User
    private var mProfileImageURL: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()


        val iv_profile_user_image = findViewById<ImageView>(R.id.img)
        iv_profile_user_image.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE)
            }
        }
        var name=findViewById<EditText>(R.id.name)
        var contact=findViewById<EditText>(R.id.mobile)
        var email=findViewById<EditText>(R.id.email)

        var userDetails:User = User()

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS))
        {
            userDetails=intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        name.isEnabled=false
        name.setText(userDetails.Username)

        contact.isEnabled=false
        contact.setText(userDetails.mobile)

        email.isEnabled=false
        name.setText(userDetails.email)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
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

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun back(view: View) {
        startActivity(Intent(this,dashboard::class.java))
    }
}
