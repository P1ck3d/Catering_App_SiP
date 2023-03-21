package com.example.a5150_courier.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a5150_courier.Callback.CategoryListner
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Model.CategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuViewModel : ViewModel(), CategoryListner {

    private var categoriesListMutable : MutableLiveData<List<CategoryModel>>?=null
    private var messageError : MutableLiveData<String> = MutableLiveData()
    private val categoryCallbackListener : CategoryListner

    init {
        categoryCallbackListener = this
    }

    fun getCategoreList() : MutableLiveData<List<CategoryModel>>{
        if (categoriesListMutable == null){
            categoriesListMutable = MutableLiveData()
            loadCategory()
        }
        return categoriesListMutable!!
    }

    fun getMessageError() : MutableLiveData<String>{
        return messageError
    }

    private fun loadCategory() {
        val tempList = ArrayList<CategoryModel>()
        val categoryRef = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF)
        categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (itemSnapShot in p0!!.children)
                {
                    val model = itemSnapShot.getValue<CategoryModel>(CategoryModel::class.java)
                    model!!.menu_id = itemSnapShot.key
                    tempList.add(model!!)
                }
                categoryCallbackListener.onCategoryLoadSuccess(tempList)
            }

            override fun onCancelled(p0: DatabaseError) {
                categoryCallbackListener.onCategoryLoadFailed((p0.message))
            }
        })
    }

    override fun onCategoryLoadSuccess(categoriesList: List<CategoryModel>) {
        categoriesListMutable!!.value = categoriesList
    }

    override fun onCategoryLoadFailed(message: String) {
        messageError.value = message
    }

}