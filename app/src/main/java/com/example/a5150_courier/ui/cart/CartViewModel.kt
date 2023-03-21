package com.example.a5150_courier.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Database.CartDataSource
import com.example.a5150_courier.Database.CartDatabase
import com.example.a5150_courier.Database.CartItem
import com.example.a5150_courier.Database.LocalCartDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartViewModel : ViewModel() {
    private val compositeDisposable:CompositeDisposable
    private var cartDataSource:CartDataSource?=null
    private var mutableLiveDataCartItem:MutableLiveData<List<CartItem>>?=null

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun initCartDataSource(context: android.content.Context){
        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(context).cartDAO())
    }

    fun getMutableLiveDataCartItem():MutableLiveData<List<CartItem>>{
        if (mutableLiveDataCartItem == null)
            mutableLiveDataCartItem = MutableLiveData()
        getCartItems()
        return mutableLiveDataCartItem!!
    }

    private fun getCartItems(){
        compositeDisposable.addAll(cartDataSource!!.getAllCart(Common.UserId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({cartItems ->
                mutableLiveDataCartItem!!.value = cartItems
            },{t: Throwable? -> mutableLiveDataCartItem!!.value = null }))
    }

    fun onStop(){
        compositeDisposable.clear()
    }
}