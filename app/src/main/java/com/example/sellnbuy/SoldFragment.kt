package com.example.sellnbuy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.SoldProducts
import com.example.sellnbuy.ui.Adapters.SoldProductsListAdapter
import kotlinx.android.synthetic.main.fragment_sold.*


class SoldFragment : baseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_sold, container, false)
    }
    override fun onResume() {
        super.onResume()

        getSoldProductsList()
    }

    private fun getSoldProductsList() {
        // Show the progress dialog.
        showPB()
        // Call the function of Firestore class.
        firestore().getSoldProductsList(this@SoldFragment)
    }

    fun successSoldProductsList(soldProductsList: ArrayList<SoldProducts>) {

        hidePB()
        if (soldProductsList.size > 0) {
            sold_rv.visibility = View.VISIBLE
            empty_text_sold.visibility = View.GONE

            sold_rv.layoutManager = LinearLayoutManager(activity)
            sold_rv.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductsListAdapter(requireActivity(), soldProductsList)
            sold_rv.adapter = soldProductsListAdapter
        } else {
            sold_rv.visibility = View.GONE
            empty_text_sold.visibility = View.VISIBLE
        }
    }
}