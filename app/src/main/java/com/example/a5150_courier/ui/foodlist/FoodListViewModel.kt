package com.example.a5150_courier.ui.foodlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Model.FoodModel

/*class FoodListViewModel {
    private lateinit var foodListViewModel: FoodListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View?{
        foodListViewModel =
            ViewModelProviders.of(this).get(foodListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_food_list, container, )
        val textView: TextView = root.findViewById(R.id.text_food_list)
        foodListViewModel.text.observe(this, Observer {
            textView.text=it
        })
    return root
    }
    )
}*/

class FoodListViewModel : ViewModel(){
    private var mutableFoodModelListData : MutableLiveData<List<FoodModel>>?=null
            fun getMutableFoodModelListData(): MutableLiveData<List<FoodModel>>{
                if (mutableFoodModelListData == null)
                    mutableFoodModelListData = MutableLiveData()
                mutableFoodModelListData!!.value = Common.categorySelected!!.foods
                return mutableFoodModelListData!!
            }
}