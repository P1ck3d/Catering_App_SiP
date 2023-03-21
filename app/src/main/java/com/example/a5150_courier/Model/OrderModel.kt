package com.example.a5150_courier.Model

import com.example.a5150_courier.Database.CartItem

class OrderModel {
    var uid:String?=null
    var uName:String?=null
    var uPhone:String?=null
    var shipmentadr:String?=null
    var transID:String?=null
    var lat:Double = 0.toDouble()
    var lng:Double = 0.toDouble()
    var fPayment:Double = 0.toDouble()
    var tPayment:Double = 0.toDouble()
    var Cod:Boolean = false
    var cartItemList:List<CartItem>?=null
}