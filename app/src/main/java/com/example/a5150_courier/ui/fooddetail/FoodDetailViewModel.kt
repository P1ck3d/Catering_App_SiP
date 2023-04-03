package com.example.a5150_courier.ui.fooddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Model.FoodModel

class FoodDetailViewModel : ViewModel() {

    private var mutableLiveDataFood:MutableLiveData<FoodModel>?=null


    init {

    }

    fun getMutableLiveDataFood():MutableLiveData<FoodModel>{
        if (mutableLiveDataFood == null)
            mutableLiveDataFood = MutableLiveData()
            mutableLiveDataFood!!.value = Common.foodSelected
        return mutableLiveDataFood!!
    }


    fun setFoodModel(foodModel: FoodModel) {
        if (mutableLiveDataFood != null)
            mutableLiveDataFood!!.value = foodModel
    }


}