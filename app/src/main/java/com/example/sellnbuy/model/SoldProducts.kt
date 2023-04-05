package com.example.sellnbuy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoldProducts (
    val user_id:String="",
    val title: String="",
    val price: String ="",
    val sold_quantity:String="",
    val image:String="",
    val order_id:String="",
    val order_date:String="",
    val sub_total_anount:String="",
    val shipping_charge:String="",
    val total_amount:String="",
    val address:String="",
    var id:String="",
    ): Parcelable
