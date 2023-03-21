package com.example.a5150_courier.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asksira.loopingviewpager.LoopingViewPager
import com.example.a5150_courier.Adapter.BestDeals
import com.example.a5150_courier.Adapter.PopularCategories
import com.example.a5150_courier.R

class HomeFragment : Fragment() {

    //var homeViewModel //homeViewModel

    var recyclerView:RecyclerView?=null
    var viewPager:LoopingViewPager?=null

    var layoutAnimationController:LayoutAnimationController?=null
    //private var _binding: FragmentHomeBinding? = null

    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          var homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //_binding = FragmentHomeBinding.inflate(inflater, container, false)

        //val root: View = binding.root
        initView(root)
        homeViewModel.popularList.observe(this, Observer{
            val listData = it
            val adapter = PopularCategories(requireContext(),listData)
            recyclerView!!.adapter = adapter
            recyclerView!!.layoutAnimation = layoutAnimationController
        })
        homeViewModel.bestDealList.observe(viewLifecycleOwner, Observer{
            val adapter = BestDeals(requireContext(), it, false)
            viewPager!!.adapter = adapter
        })
        return root
    }

    private fun initView(root:View) {
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_item_from_left)

        viewPager = root.findViewById((R.id.viewpager)) as LoopingViewPager
        viewPager!!.setBackgroundColor(Color.rgb(188, 170, 164))
        recyclerView = root.findViewById(R.id.recycler_popular) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager  = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
    }


    override fun onResume() {
        super.onResume()
        viewPager!!.resumeAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        viewPager!!.pauseAutoScroll()
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}