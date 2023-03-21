package com.example.a5150_courier.Adapter

import android.graphics.Color
import android.graphics.Color.rgb
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a5150_courier.Callback.RecyclerItemClick
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Database.CartDataSource
import com.example.a5150_courier.Database.CartDatabase
import com.example.a5150_courier.Database.CartItem
import com.example.a5150_courier.Database.LocalCartDataSource
import com.example.a5150_courier.EventBus.FoodItemClick
import com.example.a5150_courier.Model.FoodModel
import com.example.a5150_courier.R
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus


class FoodList(internal var context: android.content.Context,
               internal var foodList: List<FoodModel>) :
    RecyclerView.Adapter<FoodList.MyViewHolder>(){

    private val compositeDisposable : CompositeDisposable
    private val cartDataSource : CartDataSource

    init{
        compositeDisposable = CompositeDisposable()
        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(context).cartDAO())
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        var txt_food_name: TextView?=null
        var txt_food_price: TextView?=null


        var img_food_image: ImageView?=null
        var img_fav: ImageView?=null
        var img_cart: ImageView?=null

       // internal var listener: IRecyclerItemClickListener?=null

        /*fun setListener(listener: IRecyclerItemClickListener){
            this.listener = listener
        }*/

        internal var listener: RecyclerItemClick?=null

        fun setListener(listener: RecyclerItemClick) {
            this.listener = listener
            itemView.setOnClickListener(this)
        }

        init {
            txt_food_name = itemView.findViewById(R.id.txt_food_name) as TextView
            txt_food_price = itemView.findViewById(R.id.txt_food_price) as TextView
            img_food_image = itemView.findViewById(R.id.img_food_id) as ImageView
            img_cart = itemView.findViewById(R.id.img_quick_cart) as ImageView
            img_fav = itemView.findViewById(R.id.img_fav) as ImageView

        }

        override fun onClick(view: View?) {
            listener!!.onItemClick(view!!,adapterPosition)
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodList.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_food_item,parent, false))
    }



    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(foodList.get(position).image).into(holder.img_food_image!!)
        holder.txt_food_name!!.setText(foodList.get(position).name)
        holder.txt_food_price!!.setText(foodList.get(position).price.toString())
        holder.txt_food_price!!.append(" zł")
        holder.txt_food_price!!.setTextColor(Color.rgb(188, 170, 164))
        holder.txt_food_name!!.setTextColor(Color.rgb(188, 170, 164))
        holder.img_cart!!.setColorFilter(rgb(188, 170, 164))
        holder.img_fav!!.setColorFilter(rgb(188, 170, 164))



        //Event
        holder.setListener(object : RecyclerItemClick{
            override fun onItemClick(view: View, pos: Int) {
                Common.foodSelected = foodList.get(pos)
                Common.foodSelected!!.key = pos.toString()
                EventBus.getDefault().postSticky(FoodItemClick(true,foodList.get(pos)))
            }

        })

        holder.img_cart!!.setOnClickListener {
            val cartItem = CartItem()
            cartItem.uid = Common.UserId
            //cartItem.userPhone = Common.currentUser!!.phone
            cartItem.foodId = foodList.get(position).id!!
            cartItem.foodName = foodList.get(position).name!!
            cartItem.foodImage = foodList.get(position).image!!
            cartItem.foodPrice = foodList.get(position).price.toDouble()
            cartItem.foodQuantity = 1
            cartItem.foodExtra = 0.0
            cartItem.foodAddon = "Default"
            cartItem.foodSize = "Default"
            /*compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(context, "Add to cart success", Toast.LENGTH_SHORT).show()
                    //Sdelat schetchik, esli budet vremia(14)
                },{t:Throwable? -> Toast.makeText(context, "[Insert cart]"+t!!.message,Toast.LENGTH_SHORT).show()}))*/

            cartDataSource.getItemWithAllInCart(Common.UserId, cartItem.foodId, cartItem.foodAddon, cartItem.foodSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:SingleObserver<CartItem>{
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(cartItemFromDB: CartItem) {
                        if (cartItemFromDB.equals(cartItem)){
                            //esli on uzhe v baze
                            cartItemFromDB.foodExtra = cartItem.foodExtra
                            cartItemFromDB.foodSize = cartItem.foodSize
                            cartItemFromDB.foodAddon = cartItem.foodAddon
                            cartItemFromDB.foodQuantity = cartItemFromDB.foodQuantity + cartItem.foodQuantity

                            cartDataSource.updateCart(cartItemFromDB)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object:SingleObserver<Int>{
                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onSuccess(t: Int) {
                                        Toast.makeText(context, "Successful update",Toast.LENGTH_SHORT).show()
                                        //EventBus.getDefault().postSticky(Count)
                                    }

                                    override fun onError(e: Throwable) {
                                        Toast.makeText(context, "[update]"+e.message,Toast.LENGTH_SHORT).show()
                                    }

                                })
                        }else{ //esli on nedostupen
                            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(context, "Add to cart success", Toast.LENGTH_SHORT).show()
                                    //Sdelat schetchik, esli budet vremia(14)
                                },{t:Throwable? -> Toast.makeText(context, "[Insert cart]"+t!!.message,Toast.LENGTH_SHORT).show()}))
                        }
                        }


                    override fun onError(e: Throwable) {
                        if(e.message!!.contains("empty")){
                            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(context, "Add to cart success", Toast.LENGTH_SHORT).show()
                                    //Sdelat schetchik, esli budet vremia(14)
                                },{t:Throwable? -> Toast.makeText(context, "[Insert cart]"+t!!.message,Toast.LENGTH_SHORT).show()}))
                        }else
                            Toast.makeText(context, "[Cart Err]"+e.message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
        //Common.currentUser!!.uid1
        //Common.currentUser!!.uid1

       /* fun onStop(){
            if (compositeDisposable != null)
                compositeDisposable.clear()
        }*/

    }

    fun onStop() {
        compositeDisposable.clear()
    }


}