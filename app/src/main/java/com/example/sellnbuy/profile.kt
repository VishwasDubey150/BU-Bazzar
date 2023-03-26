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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import com.bumptech.glide.Glide
import com.example.sellnbuy.Constants.showImageChooser
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException

class profile : baseActivity() {
    private lateinit var mUserDetail: User

    @SuppressLint("RestrictedApi")
    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mselectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String=""

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
        var name = findViewById<EditText>(R.id.name)
        var contact = findViewById<EditText>(R.id.mobile)
        var email = findViewById<TextView>(R.id.email)


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetail = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        name.setText(mUserDetail.Username)
        contact.setText(mUserDetail.mobile)
        email.setText(mUserDetail.email)
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

    private fun validateDetails(): Boolean {
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<TextView>(R.id.email)
        val contact = findViewById<EditText>(R.id.mobile)
        val address = findViewById<EditText>(R.id.address)
        val gender = findViewById<RadioGroup>(R.id.gender)
        val male = findViewById<RadioButton>(R.id.m)
        val female = findViewById<RadioButton>(R.id.f)
        val img = findViewById<ImageView>(R.id.img)

        return when {
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

            !male.isChecked and !female.isChecked -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun next(view: View) {
        if (validateDetails()) {
            showPB()
            if(mselectedImageFileUri != null)
            {
                firestore().uploadImageToCloudStorage(this,mselectedImageFileUri)
            }
            else
            {
                updateUserProfileDetails()
            }

        } else {
            Toast.makeText(this, "Please complete your Profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserProfileDetails()
    {
        var name = findViewById<EditText>(R.id.name)
        var contact = findViewById<EditText>(R.id.mobile)
        var email = findViewById<TextView>(R.id.email)
        var addresss = findViewById<EditText>(R.id.address)
        var male = findViewById<RadioButton>(R.id.m)
        var female = findViewById<RadioButton>(R.id.f)

        val userHashMap = HashMap<String, Any>()

        val mobileNumber = contact.text.toString().trim { it <= ' ' }
        val address = addresss.text.toString().trim { it <= ' ' }


        val gender = if (male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber
        }


        if(mUserProfileImageURL.isNotEmpty())
        {
            userHashMap[Constants.IMAGE]=mUserProfileImageURL
        }

        if (address.isNotEmpty()) {
            userHashMap[Constants.ADDRESS] = address
        }

        userHashMap[Constants.PROFILE_COMPLETED]=1

        userHashMap[Constants.GENDER] = gender
        firestore().updateUserProfileData(this, userHashMap)

    }


    fun userProfileUpdateSuccess() {
        hidePB()
        Toast.makeText(this@profile, "profile updated successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@profile, dashboard::class.java))
        finish()
    }

    fun email(view: View) {
        Toast.makeText(this@profile, "You can't edit email id", Toast.LENGTH_SHORT).show()
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL=imageURL
        updateUserProfileDetails()
    }
}