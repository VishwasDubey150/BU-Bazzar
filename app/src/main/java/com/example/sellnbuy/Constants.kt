package com.example.sellnbuy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS:String="users"
    const val Sellnbuy_pref:String="Sellnbuy_prefs"
    const val Loggedin_un:String="loggedin_un"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val EXTRA_USER_DETAILS: String="extra_user_details"
    const val USERNAME="username"
    const val MALE: String="male"
    const val FEMALE: String="female"
    const val ADDRESS:String="address";
    const val MOBILE:String="mobile";
    const val GENDER:String="gender"

    const val EMAIL:String="email";
    const val IMAGE:String="image";
    const val PROFILE_COMPLETED:String="profileCompleted";
    const val USER_PROFILE_IMAGE:String="User_Profile_Image";


    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?
    {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}