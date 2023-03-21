package com.example.a5150_courier.Callback

import com.example.a5150_courier.Model.PopularCategoryModel

interface PopularLoad {
    fun onPopularLoadSuccess(popularModelList: List<PopularCategoryModel>)
    fun onPopularLoadFailed(message:String)
}