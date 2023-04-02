package com.example.sellnbuy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class User(
    val id:String="",
    val Username:String="",
    val email:String="",
    val mobile: String ="",
    val image:String="",
    val gender:String="",
    val address:String="",
    var profileCompleted: Int=0):Parcelable
