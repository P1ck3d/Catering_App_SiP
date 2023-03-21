/*package com.example.a5150_courier.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a5150_courier.Callback.IRecyclerItemClickListener
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.EventBus.CategoryClick
import com.example.a5150_courier.Model.CategoryModel
import com.example.a5150_courier.R
import org.greenrobot.eventbus.EventBus

class MyCategoriesAdapter(internal var context: View,
                          internal var categoriesList: List<CategoryModel>) :
    RecyclerView.Adapter<MyFoodListAdapter.MyViewHolder>(){
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        var category_name: TextView?=null

        var category_image: ImageView?=null

        internal var listener:IRecyclerItemClickListener?=null

        fun setListener(listener: IRecyclerItemClickListener){
            this.listener = listener
        }

        init {
            category_name = itemView.findViewById(R.id.category_name) as TextView
            category_image = itemView.findViewById(R.id.category_image) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listener!!.onItemClick(view!!,adapterPosition)
        }


    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(categoriesList.get(position).image).into(holder.category_image!!)
        holder.category_name!!.setText(categoriesList.get(position).name)

        //Event
        holder.setListener(object : IRecyclerItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                Common.categorySelected = categoriesList.get(pos)
                EventBus.getDefault().postSticky(CategoryClick(true,categoriesList.get(pos)))
            }
        })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyCategoriesAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent, false))
    }



    override fun getItemCount(): Int {
        return categoriesList.size
    }



    override fun getItemViewType(position: Int): Int {
        return if (categoriesList.size == 1)
            Common.DEFAULT_COLUMN_COUNT
        else{
            if (categoriesList.size % 2 == 0)
                Common.DEFAULT_COLUMN_COUNT
            else
            if (position > 1 && position == categoriesList.size-1) Common.FULL_WIDTH_COLUMN else Common.DEFAULT_COLUMN_COUNT
        }
    }



}*/

@file:Suppress("PackageName")

package com.example.a5150_courier.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a5150_courier.Callback.RecyclerItemClick
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.EventBus.CategoryClick
import com.example.a5150_courier.Model.CategoryModel
import com.example.a5150_courier.R
import org.greenrobot.eventbus.EventBus

@Suppress("DEPRECATION", "PropertyName")
class Categories(internal var context: android.content.Context,
                 internal var categoriesList: List<CategoryModel>) :
    RecyclerView.Adapter<Categories.MyViewHolder>(){
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        var category_name: TextView?=null

        var category_image: ImageView?=null

        internal var listener:RecyclerItemClick?=null

        fun setListener(listener: RecyclerItemClick) {
            this.listener = listener
        }

        init {
            category_name = itemView.findViewById(R.id.category_name) as TextView
            category_image = itemView.findViewById(R.id.category_image) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listener!!.onItemClick(view!!,adapterPosition)
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Categories.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent, false))
    }



    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(categoriesList.get(position).image).into(holder.category_image!!)
        holder.category_name!!.text = categoriesList.get(position).name
        //holder.category_name!!.setTextColor(Color.rgb(188, 170, 164))
        //Event
        holder.setListener(object : RecyclerItemClick {
            override fun onItemClick(view: View, pos: Int) {
                Common.categorySelected = categoriesList.get(pos)
                EventBus.getDefault().postSticky(CategoryClick(true,categoriesList.get(pos)))
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (categoriesList.size == 1)
            Common.DEFAULT_COLUMN_COUNT
        else{
            if (categoriesList.size % 2 == 0)
                Common.DEFAULT_COLUMN_COUNT
            else
                if (position > 1 && position == categoriesList.size-1) Common.FULL_WIDTH_COLUMN else Common.DEFAULT_COLUMN_COUNT
        }
    }
}