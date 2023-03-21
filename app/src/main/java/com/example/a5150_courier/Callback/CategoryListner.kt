package com.example.a5150_courier.Callback

import com.example.a5150_courier.Model.CategoryModel

interface CategoryListner {
    fun onCategoryLoadSuccess(categoriesList : List<CategoryModel>)
    fun onCategoryLoadFailed(message:String)
}
