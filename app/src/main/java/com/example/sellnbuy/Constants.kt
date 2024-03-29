package com.example.sellnbuy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS:String="users"
    const val PRODUCTS : String="products"
    const val PRODUCT_ID : String="product_id"
    const val Sellnbuy_pref:String="Sellnbuy_prefs"
    const val Loggedin_un:String="loggedin_un"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val EXTRA_DASH_ID: String="extra_dash_id"
    const val EXTRA_PROODUCT_ID: String="extra_product_id"
    const val EXTRA_USER_DETAILS: String="extra_user_details"
    const val EXTRA_PRODUCT_DETAILS: String="extra_product_details"
    const val USERNAME="username"
    const val EXTRA_PRODUCT_OWNER_ID: String="extra_product_owner_id"
    const val MALE: String="male"
    const val FEMALE: String="female"
    const val ADDRESS:String="address";
    const val MOBILE:String="mobile";
    const val GENDER:String="gender"
    const val PRODUCT_IMAGE="Product_Image"
    const val EMAIL:String="email";
    const val IMAGE:String="image";
    const val USER_ID:String="user_id";
    const val PROFILE_COMPLETED:String="profileCompleted";
    const val USER_PROFILE_IMAGE:String="User_Profile_Image";
    const val DEFAULT_CART_QUANTITY:String="1";
    const val CART_ITEMS:String="cart_items";
    const val ORDERS: String="orders"
    const val STOCK_QUANTITY: String="stock_quantity"
    const val SOLD_PRODUCTS: String="sold_products"

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