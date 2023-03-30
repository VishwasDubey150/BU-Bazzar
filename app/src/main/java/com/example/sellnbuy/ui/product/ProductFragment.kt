package com.example.sellnbuy.ui.product

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellnbuy.AddProductActivity
import com.example.sellnbuy.R
import com.example.sellnbuy.baseFragment
import com.example.sellnbuy.databinding.FragmentProductBinding
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.Product
import com.example.sellnbuy.ui.Adapters.MyProductListAdapters
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment : baseFragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun  successProductsListFromFireStore(productsList: ArrayList<Product>)
    {

        hidePB()

        if(productsList.size>0)
        {
            empty_text.visibility=View.GONE
            prod_rv.visibility=View.VISIBLE

            prod_rv.layoutManager=LinearLayoutManager(activity)
            val adapterProducts = MyProductListAdapters(requireActivity(),productsList,this)
            prod_rv.adapter=adapterProducts
        }

        else
        {
            empty_text.visibility=View.VISIBLE
            prod_rv.visibility=View.GONE
        }
    }

    private fun getProductListFromFireStore()
    {
        showPB()
        firestore().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        when (id)
        {
            R.id.add_product->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    _binding = null
    }

    fun deleteProduct(productID: String)
    {
        showAlertDialogToDeleteProduct(productID)
    }

    fun productDeleteSuccess() {
        hidePB()
        Toast.makeText(requireActivity(),"deleted",Toast.LENGTH_SHORT).show()
        getProductListFromFireStore()
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle("Delete")
        //set message for alert dialog
        builder.setMessage("Wanna delete this product?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            showPB()

            // Call the function of Firestore class.
            firestore().deleteProduct(this@ProductFragment, productID)
            // END

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton("no") { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}