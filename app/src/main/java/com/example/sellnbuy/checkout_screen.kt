package com.example.sellnbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.CartItem
import com.example.sellnbuy.model.Order
import com.example.sellnbuy.model.Product
import com.google.firebase.firestore.auth.User
import com.myshoppal.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_checkout_screen.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_cart_layout.*

class checkout_screen : baseActivity() {
    private lateinit var mProductsList: ArrayList<Product>

    var mAdress:String=""
    var mContact:String=""
    var ctotal:String=""
    private var mtotal:Int=0

    private lateinit var mCartItemsList: ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_screen)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        rv_ck.visibility=View.VISIBLE

        getProductList()
        usermobile(user = com.example.sellnbuy.model.User())


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
        mAdress=user.address
        hidePB()
    }

    fun usermobile(user: com.example.sellnbuy.model.User) {

        mContact=user.address

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

        for(item in mCartItemsList)
        {
            val price=item.price.toInt()
            mtotal=mtotal+price
        }
        cost.text="₹${mtotal.toString()}"
        ctotal=mtotal.toString()



    }

    private fun getProductList() {
        firestore().getAllProductsList(this@checkout_screen)
    }

    private fun placeAnOrder()
    {
        showPB()

        val order=Order(
            firestore().getCurrentUserID(),
            mCartItemsList,
            mAdress,
            "My order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            ctotal,
            mContact
        )

        firestore().placeOrder(this,order)

    }

    fun checkout(view: View) {
        placeAnOrder()
    }

    fun orderplacedSuccess() {

        hidePB()
        Toast.makeText(this,"Your order is placed Successfully",Toast.LENGTH_SHORT).show()

        val intent=Intent(this, dashboard::class.java)

        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
        //firestore().updateAllDetails(this,mCartItemsList)
    }

    fun allDetailsUpdatedSuccessfully() {
        hidePB()
        Toast.makeText(this,"Your order is placed Successfully",Toast.LENGTH_SHORT).show()

        val intent=Intent(this, dashboard::class.java)

        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

}

