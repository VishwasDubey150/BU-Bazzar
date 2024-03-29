package com.example.sellnbuy

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

    class GlideLoader(val context: Context) {

        fun loadUserPicture(image: Any, imageView: ImageView) {
            try {
                Glide
                    .with(context)
                    .load(image) // URI of the image
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_launcher_background) // A default place holder if image is failed to load.
                    .into(imageView) // the view in which the image will be loaded.
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun loadProductPicture(image: Any, imageView: ImageView) {
            try {
                Glide
                    .with(context)
                    .load(image) // URI of the image
                    .centerCrop() // Scale type of the image.// A default place holder if image is failed to load.
                    .into(imageView) // the view in which the image will be loaded.
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }