package com.example.a5150_courier.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CartDAO {
    @Query("SELECT * FROM Cart WHERE uid=:uid")
    fun getAllCart(uid:String):Flowable<List<CartItem>>

    @Query("SELECT SUM(foodQuantity) FROM Cart WHERE uid=:uid")
    fun countItemInCart(uid:String): Single<Int>

    @Query("SELECT SUM(foodPrice+foodExtra)*foodQuantity FROM Cart WHERE uid=:uid")
    fun sumPrice(uid:String):Single<Double>

    @Query("SELECT * FROM Cart WHERE foodId=:foodId AND uid=:uid")
    fun getItemInCart(foodId:String, uid:String):Single<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAll(vararg cartItems: CartItem):Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCart(cart: CartItem):Single<Int>

    @Delete
    fun deleteCart(cart: CartItem):Single<Int>

    @Query("DELETE FROM Cart WHERE uid=:uid")
    fun cleanCart(uid: String):Single<Int>

    @Query("SELECT * FROM Cart WHERE foodId=:foodId AND uid=:uid AND foodSize=:foodSize AND foodAddon=:foodAddon")
    fun getItemWithAllInCart(foodId:String, uid:String, foodSize:String ,foodAddon:String ):Single<CartItem>

}