package com.example.a5150_courier.Common


import android.os.health.UidHealthStats
import com.example.a5150_courier.HomeActivity
import com.example.a5150_courier.Model.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat

object Common {
    fun formatPrice(price: Double): String {
        if (price != 0.toDouble()) {
            val df = DecimalFormat("#,##0.00")
            df.roundingMode = RoundingMode.HALF_UP
            val finalPrice = StringBuilder(df.format(price)).toString()
            return finalPrice.replace(".", ",")
        } else
            return "0,00"

    }

    fun calculateExtra(
        userSelectedAddon: MutableList<AddonModel>?,
        userSelectedSize: SizeModel?
    ): Double {
        var result:Double=0.0
        if (userSelectedSize == null && userSelectedAddon == null)
            return 0.0
        else if (userSelectedSize == null){
            for (addonModel in userSelectedAddon!!)
                result += addonModel.price.toDouble()
            return result
        }else if (userSelectedAddon == null){
            result = userSelectedSize.price.toDouble()
            return result
        }else{
            result = userSelectedSize.price.toDouble()
            for (addonModel in userSelectedAddon)
                result += addonModel.price.toDouble()
            return result
        }
    }


    var foodSelected: FoodModel?=null
    var categorySelected: CategoryModel?=null
    val CATEGORY_REF: String ="Category"
    val FULL_WIDTH_COLUMN: Int = 1
    val DEFAULT_COLUMN_COUNT: Int = 0
    val BEST_DEALS_REF: String="BestDeals"
    val POPULAR_REF: String="MostPopular"
    val USER_REFERENCE="Users"
    var currentUser:UserModel ?= null

    //lateinit var UserId: String

    var UserId = Firebase.auth.currentUser!!.uid
    var uid1:String?=null

}