package com.example.sellnbuy

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class baseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
    fun showErrorSnackBar(message: String,errorMessage: Boolean)
    {
        val snackbar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_SHORT)
        val snackbarView=snackbar.view
        if(errorMessage)
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@baseActivity,
                android.R.color.holo_red_dark))
        }
        else
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@baseActivity,
                android.R.color.holo_green_dark))
        }
        snackbar.show()
    }

    fun showPB()
    {
        mProgressDialog= Dialog(this)
        mProgressDialog.setContentView(R.layout.progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hidePB()
    {
        mProgressDialog.dismiss()
    }
}