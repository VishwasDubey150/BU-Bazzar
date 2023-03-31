package com.example.sellnbuy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val user_id:String="",
    var product_id: String="",
    val title:String="",
    val price: String ="",
    var image: String="",
    var cart_quantity: String="",
    val stock_quantity:String="",
    val id:String="",
): Parcelable