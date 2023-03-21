package com.example.a5150_courier.Database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CartDataSource {
    fun getAllCart(uid:String): Flowable<List<CartItem>>

    fun countItemInCart(uid:String): Single<Int>

    fun sumPrice(uid:String): Single<Double>

    fun getItemInCart(foodId:String, uid:String): Single<CartItem>

    fun insertOrReplaceAll(vararg cartItems: CartItem): Completable

    fun updateCart(cart: CartItem): Single<Int>

    fun deleteCart(cart: CartItem): Single<Int>

    fun cleanCart(uid: String): Single<Int>

    fun getItemWithAllInCart(foodId:String, uid:String, foodSize:String, foodAddon:String ):Single<CartItem>
}