package com.example.sellnbuy.ui.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellnbuy.baseFragment
import com.example.sellnbuy.databinding.FragmentOrdersBinding
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.Order
import com.example.sellnbuy.ui.Adapters.MyOrderListAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_product.*

class OrderFragment : baseFragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    fun populateOrdersListInUI(ordersList: ArrayList<Order>)
    {
        hidePB()
        if(ordersList.size>0)
        {
            order_rv.visibility=View.VISIBLE
            order_empty.visibility=View.GONE

            order_rv.layoutManager=LinearLayoutManager(activity)
            order_rv.setHasFixedSize(true)

            val myOrderAdapter = MyOrderListAdapter(requireActivity(),ordersList)
            order_rv.adapter=myOrderAdapter
        }
        else
        {
            order_rv.visibility=View.GONE
            order_empty.visibility=View.VISIBLE
        }
    }

    private fun getMyOrdersList()
    {
        showPB()
        firestore().getMyOrdersList(this@OrderFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }
}