package com.example.a5150_courier.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a5150_courier.Callback.RecyclerItemClick
import com.example.a5150_courier.EventBus.PopularFoodClick
import com.example.a5150_courier.Model.PopularCategoryModel
import com.example.a5150_courier.R
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus

class PopularCategories(internal var context: android.content.Context,
                        internal var popularCategoryModels: List<PopularCategoryModel>) :
RecyclerView.Adapter<PopularCategories.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        var category_name:TextView?=null

        var category_image:CircleImageView?=null

        internal var listener: RecyclerItemClick?=null

        fun setListener(listener: RecyclerItemClick) {
            this.listener = listener
        }

        init {
            category_name = itemView.findViewById(R.id.txt_category_name) as TextView
            category_image = itemView.findViewById(R.id.category_image) as CircleImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(v!!,adapterPosition)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_popular_categories_item,parent, false))
    }

    override fun getItemCount(): Int {
        return popularCategoryModels.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(popularCategoryModels.get(position).image).into(holder.category_image!!)
        holder.category_name!!.setText(popularCategoryModels.get(position).name)
        holder.setListener(object:RecyclerItemClick{ //PopularItemsClick
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault()
                    .postSticky(PopularFoodClick(popularCategoryModels[pos]))
            }

        })
    }

}