package com.example.sellnbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.CartItem
import com.example.sellnbuy.model.Product
import com.google.firebase.firestore.auth.User
import com.myshoppal.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_checkout_screen.*
import kotlinx.android.synthetic.main.activity_profile.*

class checkout_screen : baseActivity() {
    private lateinit var mProductsList: ArrayList<Product>


    private lateinit var mCartItemsList: ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_screen)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        rv_ck.visibility=View.VISIBLE

        getProductList()
    }

    override fun onResume() {
        super.onResume()
        getaddress()
    }

    private fun getaddress() {
        showPB()
        firestore().getaddress(this)
    }

    fun useraddressSuccess(user: com.example.sellnbuy.model.User) {

        curr_address.text = user.address
        hidePB()

    }
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList

        getCartItemsList()

    }

    private fun getCartItemsList() {
        firestore().getCartList(this@checkout_screen)
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        hidePB()
        mCartItemsList = cartList
        rv_ck.layoutManager = LinearLayoutManager(this@checkout_screen)
        rv_ck.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@checkout_screen, mCartItemsList)
        rv_ck.adapter = cartListAdapter
    }

    private fun getProductList() {
        firestore().getAllProductsList(this@checkout_screen)
    }

}

