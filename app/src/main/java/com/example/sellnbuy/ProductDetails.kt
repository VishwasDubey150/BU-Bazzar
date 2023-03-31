package com.example.sellnbuy

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.Product
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetails : baseActivity() {
    private var mProductId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (intent.hasExtra(Constants.EXTRA_PROODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PROODUCT_ID)!!
            Log.i("Product Id", mProductId)
        }

        getProductDetails()
    }

        private fun getProductDetails() {
            showPB()
            firestore().getProductDetails(this@ProductDetails, mProductId)
        }

        fun productDetailsSuccess(product: Product) {
            hidePB()
            GlideLoader(this@ProductDetails).loadProductPicture(product.p_image,pro_img)

            pro_title.text = product.title
            pro_price.text = "$${product.price}"
            pro_description.text = product.description
            pro_stock_left.text = "only ${product.stock_quantity} left!!"
        }

}
