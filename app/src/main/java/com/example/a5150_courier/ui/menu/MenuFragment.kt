/*package com.example.a5150_courier.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a5150_courier.Adapter.MyCategoriesAdapter
import com.example.a5150_courier.Adapter.MyFoodListAdapter
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Common.SpacesItemDecoration
import com.example.a5150_courier.R
import com.example.a5150_courier.databinding.FragmentCategoryBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private lateinit var  menuViewModel: MenuViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var layoutAnimationController: LayoutAnimationController
    private var adapter : MyFoodListAdapter?=null

    private var recycler_menu : RecyclerView?=null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews(root)

        menuViewModel.getMessageError().observe(this,Observer{
            Toast.makeText(context, it,Toast.LENGTH_SHORT).show()
        })

        menuViewModel.getCategoreList().observe(this, Observer{
            //dialog.dismiss()
            adapter = MyCategoriesAdapter(context,it)
            recycler_menu!!.adapter = adapter
            recycler_menu!!.layoutAnimation = layoutAnimationController
        })
        return root
    }

    private fun initViews(root: View) {
        //dialog = SpotsDialog(context)
            //.setCancelable(false).build
        //dialog.show()
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_item_from_left)
        recycler_menu = root.findViewById(R.id.recycler_menu) as RecyclerView
        recycler_menu!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter != null)
                {
                    when(adapter!!.getItemViewType(position)){
                        Common.DEFAULT_COLUMN_COUNT -> 1
                        Common.FULL_WIDTH_COLUMN -> 2
                        else -> -1
                    }
                }else
                    -1
            }

        }
        recycler_menu!!.layoutManager = layoutManager
        recycler_menu!!.addItemDecoration(SpacesItemDecoration(8))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/
package com.example.a5150_courier.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a5150_courier.Adapter.Categories
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Common.SpacesItemDecoration
import com.example.a5150_courier.R
import com.example.a5150_courier.databinding.FragmentCategoryBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private lateinit var  menuViewModel: MenuViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var layoutAnimationController: LayoutAnimationController
    private var adapter : Categories?=null

    private var recycler_menu : RecyclerView?=null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews(root)

        menuViewModel.getMessageError().observe(this,Observer{
            Toast.makeText(context, it,Toast.LENGTH_SHORT).show()
        })

        menuViewModel.getCategoreList().observe(this, Observer{
            //dialog.dismiss()
            adapter = Categories(context!!,it)
            recycler_menu!!.adapter = adapter
            recycler_menu!!.layoutAnimation = layoutAnimationController
        })
        return root
    }

    private fun initViews(root: View) {
        //dialog = SpotsDialog(context)
        //.setCancelable(false).build
        //dialog.show()
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_item_from_left)
        recycler_menu = root.findViewById(R.id.recycler_menu) as RecyclerView
        recycler_menu!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter != null)
                {
                    when(adapter!!.getItemViewType(position)){
                        Common.DEFAULT_COLUMN_COUNT -> 1
                        Common.FULL_WIDTH_COLUMN -> 2
                        else -> -1
                    }
                }else
                    -1
            }

        }
        recycler_menu!!.layoutManager = layoutManager
        recycler_menu!!.addItemDecoration(SpacesItemDecoration(8))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}