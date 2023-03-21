package com.example.a5150_courier.Callback

import com.example.a5150_courier.Model.BestDealModel

interface BestDealLoad {
    fun onBestDealLoadSuccess(BestDealList: List<BestDealModel>)
    fun onBestDealLoadFailed(message:String)
}