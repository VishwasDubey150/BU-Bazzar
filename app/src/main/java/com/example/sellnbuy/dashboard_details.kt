package com.example.sellnbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.CartItem
import com.example.sellnbuy.model.Product
import kotlinx.android.synthetic.main.activity_dashboard_details.*
import kotlinx.android.synthetic.main.activity_product_details.*

class dashboard_details : baseActivity() {
    private var mProductId:String=""
    private lateinit var mProductDetails: Product
    private var mProductOwnerId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_details)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (intent.hasExtra(Constants.EXTRA_DASH_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_DASH_ID)!!
            Log.i("Product Id", mProductId)
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!

        }

        if(firestore().getCurrentUserID()==mProductOwnerId)
        {
            add_to_cart.visibility=View.GONE
            go_to_cart.visibility=View.GONE
            added.visibility=View.GONE
        }
        else
        {
            add_to_cart.visibility=View.VISIBLE
        }

        getProductDetais()

    }

    private fun getProductDetais() {

        showPB()
        firestore().getdashbordDetails(this, mProductId)
    }

    fun dashDetailsSuccess(product: Product) {
        mProductDetails=product
        hidePB()
        GlideLoader(this@dashboard_details).loadProductPicture(product.p_image,dash_img)
        dash_title.text = product.title
        dash_price.text = "₹${product.price}"
        dash_description.text = product.description
        dash_stock_left.text = "only ${product.stock_quantity} left!!"
    }

    private fun AddToCart()
    {
        val atc=CartItem(
            firestore().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.p_image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showPB()
        firestore().addCartItems(this,atc)
    }

    fun addtocart(view: View) {
        Toast.makeText(this,"adding",Toast.LENGTH_SHORT).show()
        AddToCart()
    }

    fun addtoCartSuccess() {
        hidePB()
        Toast.makeText(this,"adding",Toast.LENGTH_SHORT).show()
        add_to_cart.visibility=View.GONE
        go_to_cart.visibility=View.VISIBLE
        added.visibility=View.VISIBLE
    }

    fun gotocart(view: View) {
        startActivity(Intent(this,CartList::class.java))
    }
    fun productExistsInCart() {
        hidePB()
        add_to_cart.visibility=View.GONE
        go_to_cart.visibility=View.VISIBLE
        added.visibility=View.VISIBLE
    }
}
