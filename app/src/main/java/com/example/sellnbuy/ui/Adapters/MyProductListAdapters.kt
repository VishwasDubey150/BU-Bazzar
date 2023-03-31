package com.example.sellnbuy.ui.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellnbuy.*
import com.example.sellnbuy.databinding.ActivityProductDetailsBinding
import com.example.sellnbuy.model.Product
import com.example.sellnbuy.ui.product.ProductFragment
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class MyProductListAdapters(
    private val context: Context,
    private var list: ArrayList<Product>,
    private var fragment: ProductFragment
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder)
        {
            GlideLoader(context).loadProductPicture(model.p_image,holder.itemView.product_img)
            holder.itemView.item_name.text=model.title
            holder.itemView.item_price.text="₹${model.price}"

            holder.itemView.delete.setOnClickListener {
                fragment.deleteProduct(model.product_id)
            }


            holder.itemView.setOnClickListener {
                val intent=Intent(context,ProductDetails::class.java)
                intent.putExtra(Constants.EXTRA_PROODUCT_ID,model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}