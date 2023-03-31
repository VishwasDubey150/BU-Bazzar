package com.example.sellnbuy.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sellnbuy.CartList
import com.example.sellnbuy.R
import com.example.sellnbuy.SettingActivity
import com.example.sellnbuy.baseFragment
import com.example.sellnbuy.databinding.FragmentDashboardBinding
import com.example.sellnbuy.firestore.firestore
import com.example.sellnbuy.model.Product
import com.example.sellnbuy.ui.Adapters.dashboaedListAdapter
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : baseFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    //    val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        when (id)
        {
            R.id.action_setting->{
                startActivity(Intent(activity, SettingActivity::class.java))
                return true
            }
            R.id.action_cart->{
                startActivity(Intent(activity, CartList::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemsList :ArrayList<Product>)
    {
        hidePB()
        if(dashboardItemsList.size>0)
        {
            dash_rv.visibility=View.VISIBLE
            empty_text_dash.visibility=View.GONE

            dash_rv.layoutManager=GridLayoutManager(activity,2)
            dash_rv.setHasFixedSize(true)

            val adapter=dashboaedListAdapter(requireActivity(),dashboardItemsList)
            dash_rv.adapter=adapter
        }
        else
        {
            dash_rv.visibility=View.GONE
            empty_text_dash.visibility=View.VISIBLE
        }
    }

    private fun getDashboardItemsList()
    {
        showPB()
        firestore().getDashboardItemsList(this@DashboardFragment)
    }
}