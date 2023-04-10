package com.example.sellnbuy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.CartItem
import com.myshoppal.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.item_cart_layout.*

open class CartList : baseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>)
    {
        hidePB()
        if (cartList.size>0)
        {
            cart_rv.visibility=View.VISIBLE
            empty_text.visibility=View.GONE
            cardView.visibility=View.VISIBLE

            cart_rv.layoutManager=LinearLayoutManager(this@CartList)
            cart_rv.setHasFixedSize(true)

            val carListAdapter=CartItemsListAdapter(this@CartList,cartList)
            cart_rv.adapter=carListAdapter
            var total: Int=0
            for (item in cartList)
            {
                val price=item.price.toInt()
                total=price+total
            }
            total_price.text="${total}"
    }
        else
        {
            cart_rv.visibility=View.GONE
            empty_text.visibility=View.VISIBLE
            cardView.visibility=View.GONE
        }
    }

    private fun getCartItemList()
    {
        showPB()
        firestore().getCartList(this@CartList)
    }

    override fun onResume() {
        super.onResume()
        getCartItemList()
    }

    fun checkout(view: View) {
        startActivity(Intent(this,checkout_screen::class.java))
    }

    fun itemRemovedSuccess() {
        hidePB()
        Toast.makeText(
            this@CartList,"item deleted",
            Toast.LENGTH_SHORT
        ).show()

        getCartItemList()
    }
}