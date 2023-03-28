package com.example.sellnbuy

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.sellnbuy.Constants.PRODUCT_IMAGE
import com.example.sellnbuy.R
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.Product
import java.io.IOException
import java.net.URL

class AddProductActivity : baseActivity() {
    val mSelectedImageFileURI: Uri? =null
    var mProductImageURL: String=""
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

    fun submit(view: View) {

            if (validateDetails())
            {
                showPB()

                if (mSelectedImageFileURI != null)
                {
                    firestore().uploadImageToCloudStorage(this,mSelectedImageFileURI,Constants.PRODUCT_IMAGE)
                }
            }
        Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
    }

    private fun validateDetails(): Boolean {
        var title=findViewById<EditText>(R.id.product_title)
        var description=findViewById<EditText>(R.id.product_title)
        var price=findViewById<EditText>(R.id.product_title)
        var quantity=findViewById<EditText>(R.id.product_title)


        return when {

            TextUtils.isEmpty(title.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(description.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(price.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(quantity.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please complete the profile", Toast.LENGTH_SHORT).show()
                false

            }

            else -> {
                true
            }
        }
    }



    fun imageUploadSuccess(imageURL: String)
    {
        Toast.makeText(this,"Product image is Uploaded Successfully",Toast.LENGTH_SHORT).show()
        mProductImageURL=imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails()
    {
        var title=findViewById<EditText>(R.id.product_title)
        var description=findViewById<EditText>(R.id.product_title)
        var price=findViewById<EditText>(R.id.product_title)
        var quantity=findViewById<EditText>(R.id.product_title)

        val username=this.getSharedPreferences(Constants.Sellnbuy_pref,Context.MODE_PRIVATE)
            .getString(Constants.Loggedin_un,"")!!

        val product=Product(
            firestore().getCurrentUserID(),
            username,
            title.text.toString().trim{ it <= ' '},
            price.text.toString().trim{ it <=' '},
            description.text.toString().trim{ it <=' '},
            quantity.text.toString().trim{ it <=' '},
            mProductImageURL
        )
        firestore().uploadProductDetails(this,product)
    }

    fun productUploadSuccess()
    {
        hidePB()
        Toast.makeText(this@AddProductActivity,"product is added",Toast.LENGTH_SHORT).show()
        finish()

    }
}