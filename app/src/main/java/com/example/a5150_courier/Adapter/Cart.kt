package com.example.a5150_courier.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a5150_courier.Database.CartDataSource
import com.example.a5150_courier.Database.CartDatabase
import com.example.a5150_courier.Database.CartItem
import com.example.a5150_courier.Database.LocalCartDataSource
import com.example.a5150_courier.R
import io.reactivex.disposables.CompositeDisposable

class Cart (internal var context: android.content.Context,
            internal var cartItems: List<CartItem>) :
    RecyclerView.Adapter<Cart.MyViewHolder>(){

    internal var compositeDisposable:CompositeDisposable
    internal var cartDataSource:CartDataSource

    init {
        compositeDisposable = CompositeDisposable()
        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(context).cartDAO())
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var img_cart:ImageView
        var txt_food_name: TextView?=null
        var txt_food_price: TextView?=null

        init {
            img_cart = itemView.findViewById(R.id.img_cart) as ImageView
            txt_food_name = itemView.findViewById(R.id.txt_food_name) as TextView
            txt_food_price = itemView.findViewById(R.id.txt_food_price) as TextView
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_cart_item,parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(cartItems[position].foodImage)
            .into(holder.img_cart)
        holder.txt_food_name!!.text  = StringBuilder(cartItems[position].foodName!!)
        holder.txt_food_price!!.text  = StringBuilder("").append(cartItems[position].foodPrice + cartItems[position].foodExtra)
        holder.txt_food_price!!.append(" zł")
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun getItemAtPosition(pos: Int): CartItem {
        return cartItems[pos]
    }
}