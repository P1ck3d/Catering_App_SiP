package com.example.a5150_courier.ui.foodlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a5150_courier.Adapter.FoodList
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.R



class FoodListFragment : Fragment(){
    private lateinit var foodListViewModel: FoodListViewModel

    var recycler_food_list : RecyclerView?=null
    var layoutAnimationController:LayoutAnimationController?=null

    var adapter : FoodList?=null

    override fun onStop() {
        if(adapter !=null)
            adapter!!.onStop()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        foodListViewModel =
            ViewModelProvider(this).get(FoodListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_food_list, container, false)
        initViews(root)
        foodListViewModel.getMutableFoodModelListData().observe(this, androidx.lifecycle.Observer {
            adapter = FoodList(context!!,it)
            recycler_food_list!!.adapter = adapter
            recycler_food_list!!.layoutAnimation = layoutAnimationController
        })
        return root

}

    private fun initViews(root: View?) {
        recycler_food_list = root!!.findViewById(R.id.recycler_food_list) as RecyclerView
        recycler_food_list!!.setHasFixedSize(true)
        recycler_food_list!!.layoutManager = LinearLayoutManager(context)

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_item_from_left)

        (activity as AppCompatActivity).supportActionBar!!.title = Common.categorySelected!!.name

    }

    }
