package com.example.sellnbuy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Product (
        val user_id:String="",
        val title:String="",
        val price: String ="",
        val description:String="",
        val stock_quantity:String="",
        val p_image:String="",
        var product_id: String=""
): Parcelable
