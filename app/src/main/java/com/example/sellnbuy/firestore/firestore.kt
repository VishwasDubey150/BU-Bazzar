package com.example.sellnbuy.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sellnbuy.*
import com.example.sellnbuy.model.*
import com.example.sellnbuy.ui.Order.OrderFragment
import com.example.sellnbuy.ui.dashboard.DashboardFragment
import com.example.sellnbuy.ui.product.ProductFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.grpc.InternalChannelz.id
import kotlinx.android.synthetic.main.fragment_sold.*

class firestore:baseActivity() {
    private val mfirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: sign_up, userInfo: User) {
        mfirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hidePB()
                Log.e(activity.javaClass.simpleName, "Error while registering the user.", e)
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }


    fun getUserDetails(activity: Activity) {
        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!

                when (activity) {
                    is login -> {
                        activity.userLoggedInSuccess(user)
                    }

                    is SettingActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }

                val sharedPreferences = activity.getSharedPreferences(Constants.Sellnbuy_pref,Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.Loggedin_un,"${user.Username}")
                editor.apply()

                when (activity) {
                    is login -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is login -> {
                        activity.hidePB()
                    }
                    is SettingActivity -> {
                        activity.hidePB()
                    }
                }
            }
    }
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {

                // Notify the success result.
                when (activity) {
                    is profile -> {
                        activity.userProfileUpdateSuccess()
                    }

                    is SettingActivity ->{

                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is profile -> {
                        activity.hidePB()
                        Toast.makeText(this, "Something went worng", Toast.LENGTH_SHORT).show()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.", e
                )
            }
    }
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

            //getting the storage reference
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                imageType + System.currentTimeMillis() + "."
                        + Constants.getFileExtension(
                    activity,
                    imageFileURI
                )
            )

            //adding the file to reference
            sRef.putFile(imageFileURI!!)
                .addOnSuccessListener { taskSnapshot ->
                    // The image upload is success
                    Log.e(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )

                    // Get the downloadable url from the task snapshot
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())

                            // Here call a function of base activity for transferring the result to it.
                            when (activity) {
                                is profile -> {
                                    activity.imageUploadSuccess(uri.toString())
                                }

                                is AddProductActivity->{
                                    activity.imageUploadSuccess(uri.toString())

                                }
                            }
                        }
                }
                .addOnFailureListener { exception ->

                    // Hide the progress dialog if there is any error. And print the error in log.
                    when (activity) {
                        is profile -> {
                            activity.hidePB()
                        }
                        is AddProductActivity ->{
                            hidePB()
                        }
                    }
                    Log.e(
                        activity.javaClass.simpleName,
                        exception.message,
                        exception
                    )
                }
        }

    fun uploadProductDetails(activity: AddProductActivity,productInfo: Product)
    {
        mfirestore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener {
                e ->
                activity.hidePB()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.", e
                )
            }
    }

    fun getProductsList(fragment: Fragment)
    {
        mfirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List",document.documents.toString())
                val productList: ArrayList<Product> =ArrayList()
                for (i in document.documents)
                {
                    val product=i.toObject(Product::class.java)
                    product!!.product_id=i.id
                    productList.add(product)
            }
                when(fragment)
                {
                    is ProductFragment ->{
                        fragment.successProductsListFromFireStore(productList)
                    }
                }
        }
    }

    fun getDashboardItemsList(fragment: DashboardFragment)
    {
        mfirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { docunment ->
                Log.e(fragment.javaClass.simpleName,docunment.documents.toString())
                val productsList : ArrayList<Product> = ArrayList()

                for(i in docunment.documents)
                {
                    val product=i.toObject(Product::class.java)!!
                    product.product_id=i.id
                    productsList.add(product)
                }

                fragment.successDashboardItemsList(productsList)
            }

            .addOnFailureListener {
                e ->
                fragment.hidePB()
                Log.e(fragment.javaClass.simpleName,"Error while fectching",e)


            }
    }

    fun deleteProduct(fragment: ProductFragment, productId: String) {

        mfirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {

                fragment.productDeleteSuccess()

            }
            .addOnFailureListener { e->

                fragment.hidePB()

                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

    fun getProductDetails(activity: ProductDetails, productId: String) {

        // The collection name for PRODUCTS
        mfirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val product = document.toObject(Product::class.java)!!

                activity.productDetailsSuccess(product)
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hidePB()

                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }


    fun getdashbordDetails(activity: dashboard_details, productId: String) {

        // The collection name for PRODUCTS
        mfirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val product = document.toObject(Product::class.java)!!

                activity.dashDetailsSuccess(product)
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hidePB()

                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }

    fun addCartItems(activity: dashboard_details, addToCart:CartItem) {
        mfirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addtoCartSuccess()
            }
            .addOnFailureListener { e ->
                activity.hidePB()
                Log.e(activity.javaClass.simpleName, "Error while registering the user.", e)
            }
    }

    fun checkIfItemExistInCart(activity: dashboard_details, productId: String) {

        mfirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.documents.toString())

                // TODO Step 8: Notify the success result to the base class.
                // START
                // If the document size is greater than 1 it means the product is already added to the cart.
                if (document.documents.size > 0) {
                    activity.productExistsInCart()
                } else {
                    activity.hidePB()
                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error.
                activity.hidePB()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )
            }
    }

    fun getCartList(activity : Activity)
    {
        mfirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener {
                    document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val list : ArrayList<CartItem> = ArrayList()

                for (i in document.documents)
                {
                    val cartItem=i.toObject(CartItem::class.java)!!
                    cartItem.id=i.id
                    list.add(cartItem)
                }
                when(activity)
                {
                    is CartList ->{
                        activity.successCartItemsList(list)
                    }

                    is checkout_screen -> {
                        activity.successCartItemsList(list)
                    }

                }
            }
            .addOnFailureListener {
                    e-> when(activity) {
                is CartList -> {
                    activity.hidePB()
                }
            }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )

            }
    }

    fun removeItemFromCart(context: Context, cart_id: String) {

        // Cart items collection name
        mfirestore.collection(Constants.CART_ITEMS)
            .document(cart_id) // cart id
            .delete()
            .addOnSuccessListener {

                // Notify the success result of the removed cart item from the list to the base class.
                when (context) {
                    is CartList -> {
                        context.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                when (context) {
                    is CartList -> {
                        context.hidePB()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from the cart list.",
                    e
                )
            }
    }

    fun getaddress(activity: Activity) {
        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!
                when (activity) {
                    is login -> {
                        activity.userLoggedInSuccess(user)
                    }
                }

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.Sellnbuy_pref,
                    Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.Loggedin_un,
                    "${user.Username}"
                )
                editor.apply()

                when (activity) {
                    is login -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is checkout_screen-> {
                       activity.useraddressSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is login -> {
                        activity.hidePB()
                    }
                    is SettingActivity -> {
                        activity.hidePB()
                    }
                }
            }
    }



    fun getAllProductsList(activity: Activity) {
        // END
        // The collection name for PRODUCTS
        mfirestore.collection(Constants.PRODUCTS)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Products List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when (activity) {
                    is checkout_screen -> {
                        activity.successProductsListFromFireStore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (activity) {

                    is checkout_screen -> {
                        activity.hidePB()
                    }
                }

                Log.e("Get Product List", "Error while getting all product list.", e)
            }
    }

    fun placeOrder(activity: checkout_screen,order: Order)
    {
        mfirestore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderplacedSuccess()
            }
            .addOnFailureListener { e ->
                activity.hidePB()
                Log.e(activity.javaClass.simpleName, "Error while registering the user.", e)
            }
    }

    fun updateAllDetails(activity: checkout_screen,cartList: ArrayList<CartItem>,order: Order)
    {
        val writeBatch = mfirestore.batch()
        for(cartItem in cartList)
        {
            val soldProducts=SoldProducts(
                firestore().getCurrentUserID(),
                cartItem.title,
                cartItem.price,
                cartItem.cart_quantity,
                cartItem.image,
                order.title,
                order.total_amount,order.address
            )
            val documentReference=mfirestore.collection(Constants.SOLD_PRODUCTS)
                .document()

            writeBatch.set(documentReference,soldProducts)
        }

        for (cartItem in cartList)
        {
            val documentRefrence=mfirestore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentRefrence)
        }
        writeBatch.commit().addOnSuccessListener {
            activity.allDetailsUpdatedSuccessfully()
        }.addOnFailureListener { e->
            activity.hidePB()
            Log.e(activity.javaClass.simpleName, "Error while registering the user.", e)

        }
    }

    fun getMyOrdersList(fragment: OrderFragment)
    {
        mfirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document->
                val list: ArrayList<Order> =ArrayList()

                for (i in document.documents)
                {
                    val orderItem=i.toObject(Order::class.java)!!
                    orderItem.id=i.id

                    list.add(orderItem)
                }
                fragment.populateOrdersListInUI(list)
            }.addOnFailureListener { e->
                fragment.hidePB()
                Log.e(fragment.javaClass.simpleName, "Error while registering the user.", e)

            }
    }

    fun getSoldProductsList(fragment: SoldFragment) {

        mfirestore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of sold products in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Sold Products ArrayList.
                val list: ArrayList<SoldProducts> = ArrayList()

                // A for loop as per the list of documents to convert them into Sold Products ArrayList.
                for (i in document.documents) {

                    val soldProduct = i.toObject(SoldProducts::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)
                }

                fragment.successSoldProductsList(list)
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error.
                fragment.hidePB()

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting the list of sold products.",
                    e
                )
            }
    }



}






