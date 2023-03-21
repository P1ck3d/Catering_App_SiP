package com.example.a5150_courier.Database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import javax.annotation.Nonnull


@Entity(tableName = "Cart", primaryKeys = ["uid", "foodId", "foodSize", "foodAddon"])
class CartItem {
    //@PrimaryKey
    @NonNull
    @ColumnInfo(name = "foodId")
    var foodId:String=""

    @ColumnInfo(name = "foodName")
    var foodName:String?=null

    @ColumnInfo(name = "foodPrice")
    var foodPrice:Double=0.0

    @ColumnInfo(name = "foodImage")
    var foodImage:String?=null

    @NonNull
    @ColumnInfo(name = "foodAddon")
    var foodAddon:String=""

    @NonNull
    @ColumnInfo(name = "foodSize")
    var foodSize:String=""

    @ColumnInfo(name = "userPhone")
    var userPhone:String?=""

    @ColumnInfo(name = "foodQuantity")
    var foodQuantity:Int=0

    @ColumnInfo(name = "foodExtra")
    var foodExtra:Double=0.0

    @NonNull
    @ColumnInfo(name = "uid")
    var uid:String=""

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is CartItem)
            return false
        val cartItem = other as CartItem?
        return cartItem!!.foodId == this.foodId&&
                cartItem.foodAddon == this.foodAddon&&
                cartItem.foodSize == this.foodSize
    }

}