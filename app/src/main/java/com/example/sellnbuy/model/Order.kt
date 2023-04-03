package com.example.sellnbuy.model

import android.location.Address
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: String = "",
    val title: String = "",
    val image: String = "",
    val total_amount: String = "",
    val contact :String="",
    var id: String = ""
) : Parcelable
