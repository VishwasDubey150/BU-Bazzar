package com.example.sellnbuy

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import com.bumptech.glide.Glide
import com.example.sellnbuy.Constants.showImageChooser
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.User
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.IOException

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

        name.setText(userDetails.Username)

        contact.setText(userDetails.mobile)

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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val iv_profile_user_image = findViewById<ImageView>(R.id.img)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mselectedImageFileUri = data.data!!
                        Glide
                            .with(this)
                            .load(mselectedImageFileUri)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
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
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun submit(view: View) {
        if (validateDetails())
        {
            Toast.makeText(this,"Profile successfully updated",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@profile,dashboard::class.java))
        }
    }

    private fun validateDetails(): Boolean {
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val contact = findViewById<EditText>(R.id.mobile)
        val address=findViewById<EditText>(R.id.address)
        val gender=findViewById<RadioGroup>(R.id.gender)
        val male=findViewById<RadioButton>(R.id.m)
        val female=findViewById<RadioButton>(R.id.f)
        val img=findViewById<ImageView>(R.id.img)
        var pb=findViewById<ProgressBar>(R.id.pbL)

        return when {

            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false

            }
            TextUtils.isEmpty(name.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(contact.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(address.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }

            !male.isChecked and !female.isChecked-> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }
}
