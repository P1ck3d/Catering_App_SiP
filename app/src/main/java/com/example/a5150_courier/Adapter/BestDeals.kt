@file:Suppress("PackageName")

package com.example.a5150_courier.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.bumptech.glide.Glide
import com.example.a5150_courier.EventBus.BestDealClick
import com.example.a5150_courier.Model.BestDealModel
import com.example.a5150_courier.R
import org.greenrobot.eventbus.EventBus


class BestDeals(var context: Context,
                itemList: List<BestDealModel>,
                isInfinite:Boolean):LoopingPagerAdapter<BestDealModel>(itemList,isInfinite) {
    private var food_price:TextView?=null
    override fun bindView(convertView: View, listPosition: Int, viewType: Int) {
        //Inicjalizacja elementów klasy
        val imageView = convertView.findViewById<ImageView>(R.id.img_best_deal)
        val textView = convertView.findViewById<TextView>(R.id.txt_best_deal)
        val priceView = convertView.findViewById<TextView>(R.id.txt_food_price)

        //Wyświetlanie elementów na ekranie za pomocą Glide
        Glide.with(context).load(itemList!![listPosition].image).into(imageView)
        textView.text = itemList!![listPosition].name
        textView.setTextColor(Color.rgb(188, 170, 164))
        priceView.text = buildString {
        append(itemList!![listPosition].price.toString())

        //Naciśnięcie powoduje przejście do wybranego produktu
        convertView.setOnClickListener{
            EventBus.getDefault().postSticky(BestDealClick(itemList!![listPosition]))
        }
    }


    }


    override fun inflateView(viewType: Int, container: ViewGroup, listPosition: Int): View {
        return LayoutInflater.from(context)
            .inflate(R.layout.layout_best_deals_item,container, false)
    }
}