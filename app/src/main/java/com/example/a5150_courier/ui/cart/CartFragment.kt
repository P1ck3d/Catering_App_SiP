package com.example.a5150_courier.ui.cart

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a5150_courier.Adapter.Cart
import com.example.a5150_courier.Callback.DelButton
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Common.Swipe
import com.example.a5150_courier.Database.CartDataSource
import com.example.a5150_courier.Database.CartDatabase
import com.example.a5150_courier.Database.LocalCartDataSource
import com.example.a5150_courier.EventBus.HideFab
import com.example.a5150_courier.EventBus.UpdateInCart
import com.example.a5150_courier.R
import com.example.a5150_courier.databinding.FragmentCartBinding
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private var cartDataSource:CartDataSource?=null
    private var compositeDisposable:CompositeDisposable = CompositeDisposable()
    private var recyclerViewState:Parcelable?=null
    private lateinit var cartViewModel:CartViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var txt_empty_cart:TextView?=null
    var txt_total_price:TextView?=null
    var group_place_holder:CardView?=null
    var recycler_cart:RecyclerView?=null
    var adapter:Cart?=null

    override fun onResume() {
        super.onResume()
        calculateTotalPrice()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        EventBus.getDefault().postSticky(HideFab(true))

        cartViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)
        //init data source
        cartViewModel.initCartDataSource(requireContext())

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews(root)
        cartViewModel.getMutableLiveDataCartItem().observe(viewLifecycleOwner, Observer {
            if (it == null || it.isEmpty()){
                recycler_cart!!.visibility = View.GONE
                group_place_holder!!.visibility = View.GONE
                txt_empty_cart!!.visibility = View.VISIBLE
            }
            else{
                recycler_cart!!.visibility = View.VISIBLE
                group_place_holder!!.visibility = View.VISIBLE
                txt_empty_cart!!.visibility = View.GONE

                adapter = Cart(requireContext(),it)
                recycler_cart!!.adapter = adapter
            }
        })
        return root
    }

    private fun initViews(root:View) {
        setHasOptionsMenu(true)
        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(requireContext()).cartDAO())
        recycler_cart = root.findViewById(R.id.recycler_cart) as RecyclerView
        recycler_cart!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recycler_cart!!.layoutManager = layoutManager
        recycler_cart!!.addItemDecoration(DividerItemDecoration(context,layoutManager.orientation))
        txt_empty_cart = root.findViewById(R.id.txt_empty_cart) as TextView
        txt_total_price = root.findViewById(R.id.txt_total_price) as TextView
        group_place_holder = root.findViewById(R.id.group_place_holder) as CardView
        val swipe = object:Swipe(requireContext(),recycler_cart!!,200){
            override fun instantiateRemoveItemButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<RemoveItemButton>
            ) {
                buffer.add(RemoveItemButton(context!!,"Usunąć", 45,0, Color.parseColor("#121212"),object:DelButton{
                    override fun onClick(pos: Int) {
                        val deleteItem = adapter!!.getItemAtPosition(pos)
                        cartDataSource!!.deleteCart(deleteItem)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object:SingleObserver<Int>{
                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onSuccess(t: Int) {
                                    adapter!!.notifyItemRemoved(pos)
                                    sumCart()
                                    //EventBus.getDefault().postSticky(CountCartEvent(true))
                                    Toast.makeText(context,"Produkt został usunięnty",Toast.LENGTH_SHORT).show()
                                }

                                override fun onError(e: Throwable) {
                                    Toast.makeText(context,""+e.message,Toast.LENGTH_SHORT).show()
                                }

                            })

                    }

                }))
            }

        }
    }

    private fun sumCart() {
        cartDataSource!!.sumPrice(Common.UserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:SingleObserver<Double>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: Double) {
                    txt_total_price!!.text = java.lang.StringBuilder("").append((t))
                }

                override fun onError(e: Throwable) {
                    /*if(!e.message!!.contains("Query empty"))
                        Toast.makeText(context,""+e.message,Toast.LENGTH_SHORT).show()*/
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        cartViewModel.onStop()
        compositeDisposable.clear()
        EventBus.getDefault().postSticky(HideFab(false))
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onUpdateInCart(event: UpdateInCart){
        if (event.cartItem !=null){
            recyclerViewState = recycler_cart!!.layoutManager!!.onSaveInstanceState()
            cartDataSource!!.updateCart(event.cartItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:SingleObserver<Int>{
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(t: Int) {
                        calculateTotalPrice();
                        recycler_cart!!.layoutManager!!.onRestoreInstanceState(recyclerViewState)
                    }

                    override fun onError(e: Throwable) {
                        //Toast.makeText(context, "Onowić"+e.message,Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    private fun calculateTotalPrice() {
        cartDataSource!!.sumPrice(Common.UserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:SingleObserver<Double>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(price: Double) {
                    txt_total_price!!.text = StringBuilder("")
                    .append(Common.formatPrice(price)+" zł")
                    //Common.formatPrice(price)
                }

                override fun onError(e: Throwable) {
                    //Toast.makeText(context, "Onowić"+e.message,Toast.LENGTH_SHORT).show()
                }

            })

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu!!.findItem(R.id.action_settings).setVisible(false) //Hide when in cart
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.cart_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_cart){
            cartDataSource!!.cleanCart(Common.UserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:SingleObserver<Int>{
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(t: Int) {
                        Toast.makeText(context, "Produkty zostały usunięte",Toast.LENGTH_SHORT)
                        //EventBus.getDefault().postSticky(CountCartEvent(true))
                    }

                    override fun onError(e: Throwable) {
                        //Toast.makeText(context, "Onowić"+e.message,Toast.LENGTH_SHORT).show()
                    }

                })
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}





