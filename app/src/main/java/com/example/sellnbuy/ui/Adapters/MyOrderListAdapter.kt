package com.example.sellnbuy.ui.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellnbuy.GlideLoader
import com.example.sellnbuy.R
import com.example.sellnbuy.model.Order
import kotlinx.android.synthetic.main.item_list_layout.view.*

class MyOrderListAdapter (
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.product_img
            )
            holder.itemView.item_name.text = model.title
            holder.itemView.item_price.text = "$${model.total_amount}"
            holder.itemView.delete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
// END