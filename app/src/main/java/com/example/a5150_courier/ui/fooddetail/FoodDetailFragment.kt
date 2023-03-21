package  com.example.a5150_courier.ui.fooddetail

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andremion.counterfab.CounterFab
import com.bumptech.glide.Glide
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.Database.CartDataSource
import com.example.a5150_courier.Database.CartDatabase
import com.example.a5150_courier.Database.CartItem
import com.example.a5150_courier.Database.LocalCartDataSource
import com.example.a5150_courier.Model.FoodModel
import com.example.a5150_courier.R
import com.example.a5150_courier.databinding.FragmentFoodDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class FoodDetailFragment : Fragment(), TextWatcher {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var cartDataSource:CartDataSource

    private lateinit var foodDetailViewModel: FoodDetailViewModel

    private lateinit var addonBottomSheetDialog:BottomSheetDialog

    private var _binding: FragmentFoodDetailBinding? = null
    private var btnCart: CounterFab?=null
    private var img_food: ImageView?=null
    private var food_name:TextView?=null
    private var food_description:TextView?=null
    private var food_price:TextView?=null
    private var rdi_group_size:RadioGroup?=null
    private var img_add_on:ImageView?=null
    private var chip_group_user_selected_addon:ChipGroup?=null

    //add on layout
    private var chip_group_addon:ChipGroup?=null
    private var edt_search_addon:EditText?=null

    private var waitingDialog:android.app.AlertDialog?=null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         foodDetailViewModel =
            ViewModelProvider(this).get(FoodDetailViewModel::class.java)

        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews(root)



        foodDetailViewModel.getMutableLiveDataFood().observe(viewLifecycleOwner) {
            displayInfo(it)
        }

        /* foodDetailViewModel.getMutableLiveDataComment().observe(viewLifecycleOwner){
             submitRatingToFirebase(it)
         }*/
        return root
    }

    /*private fun submitRatingToFirebase(commentModel: CommentModel?) {
        waitingDialog!!.show()

        //Submit to coment ref
        FirebaseDatabase.getInstance()
            .getReference(Common.COMMENT_REF)
            .child(Common.foodSelected!!.id!!)
            .push()
            .setValue(commentModel)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    addRatingToFood(commentModel!!.ratingValue.toDouble())
                }
                waitingDialog!!.dismiss()
            }
    }

    private fun addRatingToFood(ratingValue: Double) {
        FirebaseDatabase.getInstance()
            .getReference(Common.CATEGORY_REF)
            .child(Common.categorySelected!!.menu_id!!)
            .child("foods")
            .child(Common.foodSelected!!.key!!)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        val foodModel = dataSnapshot.getValue(FoodModel::class.java)
                        foodModel!!.key = Common.foodSelected!!.key
                        //Apply Rating
                        val sumRating  = foodModel.ratingValue.plus(ratingValue)
                        val ratingCount = foodModel.ratingCount+1
                        val result = sumRating/ratingCount

                        val updateData = HashMap<String,Any>()
                        updateData["ratingValue"] = result
                        updateData["ratingCount"] = ratingCount

                        dataSnapshot.ref
                            .updateChildren(updateData)
                            .addOnCompleteListener { task ->
                                waitingDialog!!.dismiss()
                                if (task.isSuccessful)
                                {
                                    Common.foodSelected = foodModel
                                    foodDetailViewModel.setFoodModel(foodModel)
                                    Toast.makeText(context!!,"Thank you",Toast.LENGTH_SHORT).show()

                                }
                            }
                    }
                    else
                        waitingDialog!!.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    waitingDialog!!.dismiss()
                    Toast.makeText(context!!,"",Toast.LENGTH_SHORT).show()
                }

            })
    }*/

    private fun displayInfo(it: FoodModel?) {
        Glide.with(requireContext()).load(it!!.image).into(img_food!!)
        food_name!!.text = StringBuilder(it.name!!)
        food_description!!.text = StringBuilder(it.description!!)
        food_price!!.text = StringBuilder(it.price.toString()).append(" zł")


        //Set Size
        for (sizeModel in it.size)
        {
            val  radioButton = RadioButton(context)
            radioButton.setOnCheckedChangeListener{ compoundButton, b ->
                if (b)
                    Common.foodSelected!!.userSelectedSize = sizeModel
                    calculateTotalPrice()
            }
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
            radioButton.layoutParams = params
            radioButton.text = sizeModel.name
            radioButton.setTextColor(Color.rgb(188, 170, 164))
            radioButton.tag = sizeModel.price

            rdi_group_size!!.addView(radioButton)
        }
        //def 1 rad but sel
        if (rdi_group_size!!.childCount > 0){
            val radioButton = rdi_group_size!!.getChildAt(0) as RadioButton
            radioButton.isChecked = true
        }

    }

    private fun calculateTotalPrice() {
        var totalPrice = Common.foodSelected!!.price.toDouble()
        var displayPrice = 0.0

        //Addon
        if (Common.foodSelected!!.userSelectedAddon != null && Common.foodSelected!!.userSelectedAddon!!.size > 0){
            for (addonModel in Common.foodSelected!!.userSelectedAddon!!)
                totalPrice += addonModel.price.toDouble()
        }

        //Size
        totalPrice += Common.foodSelected!!.userSelectedSize!!.price.toDouble()

        displayPrice = totalPrice  //number_button.number.toInt()
        //displayPrice = Math.round(displayPrice *100.0)/100.0

        food_price!!.text = StringBuilder("").append(Common.formatPrice(displayPrice)).append(" zł").toString()
    }

    private fun initViews(root: View) {

        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(context!!).cartDAO())

        addonBottomSheetDialog = BottomSheetDialog(context!!,R.style.DialogStyle)
        val layout_user_selected_addon = layoutInflater.inflate(R.layout.layout_addon_display, null)
        chip_group_addon = layout_user_selected_addon.findViewById(R.id.chip_group_addon) as ChipGroup
        edt_search_addon = layout_user_selected_addon.findViewById(R.id.edt_search) as EditText
        addonBottomSheetDialog.setContentView(layout_user_selected_addon)

        addonBottomSheetDialog.setOnDismissListener{dialogInterface ->
            displayUserSelectedAddon()
            calculateTotalPrice()
        }


        btnCart = root.findViewById(R.id.btnCart) as CounterFab
        img_food = root.findViewById(R.id.img_food) as ImageView
        //btnRating = root.findViewById(R.id.btn_rating) as FloatingActionButton
        food_name = root.findViewById(R.id.food_name) as TextView
        food_description = root.findViewById(R.id.food_description) as TextView
        food_price = root.findViewById(R.id.food_price) as TextView
        rdi_group_size = root.findViewById(R.id.rdi_group_size) as RadioGroup
        img_add_on = root.findViewById(R.id.img_add_on) as ImageView
        chip_group_user_selected_addon = root.findViewById(R.id.chip_group_user_selected_addon) as ChipGroup
        //ratingBar = root.findViewById(R.id.ratingBar) as RatingBar
        //btnShowComment = root.findViewById(R.id.btnShowComment) as Button

        //Event
        img_add_on!!.setOnClickListener {
            if (Common.foodSelected!!.addon != null){
                displayAllAddon()
                addonBottomSheetDialog.show()
            }
        }


       /* btnRating!!.setOnClickListener{
            showDialogRating()*/

        btnCart!!.setOnClickListener{
            val cartItem = CartItem()
            cartItem.uid = Common.UserId
            //cartItem.userPhone = Common.currentUser!!.phone
            cartItem.foodId = Common.foodSelected!!.id!!
            cartItem.foodName = Common.foodSelected!!.name!!
            cartItem.foodImage = Common.foodSelected!!.image!!
            cartItem.foodPrice = Common.foodSelected!!.price.toDouble()
            cartItem.foodQuantity = 1
            cartItem.foodExtra = Common.calculateExtra(Common.foodSelected!!.userSelectedAddon, Common.foodSelected!!.userSelectedSize)
           if (Common.foodSelected!!.userSelectedAddon != null)
               cartItem.foodAddon = Gson().toJson(Common.foodSelected!!.userSelectedAddon)
            else
            cartItem.foodAddon = "Default"
            if (Common.foodSelected!!.userSelectedSize != null)
                cartItem.foodSize = Gson().toJson(Common.foodSelected!!.userSelectedSize)
            else
            cartItem.foodSize = "Default"

            cartDataSource.getItemWithAllInCart(Common.UserId, cartItem.foodId, cartItem.foodAddon, cartItem.foodSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: SingleObserver<CartItem> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(cartItemFromDB: CartItem) {
                        if (cartItemFromDB.equals(cartItem)){
                            //esli on uzhe v baze
                            cartItemFromDB.foodExtra = cartItem.foodExtra
                            cartItemFromDB.foodSize = cartItem.foodSize
                            cartItemFromDB.foodAddon = cartItem.foodAddon
                            cartItemFromDB.foodQuantity = cartItemFromDB.foodQuantity + cartItem.foodQuantity

                            cartDataSource.updateCart(cartItemFromDB)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object: SingleObserver<Int> {
                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onSuccess(t: Int) {
                                        Toast.makeText(context, "Successful update",Toast.LENGTH_SHORT).show()
                                        //EventBus.getDefault().postSticky(Count)
                                    }

                                    override fun onError(e: Throwable) {
                                        Toast.makeText(context, "[update]"+e.message,Toast.LENGTH_SHORT).show()
                                    }

                                })
                        }else{ //esli on nedostupen
                            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(context, "Add to cart success", Toast.LENGTH_SHORT).show()
                                    //Sdelat schetchik, esli budet vremia(14)
                                },{t:Throwable? -> Toast.makeText(context, "[Insert cart]"+t!!.message,Toast.LENGTH_SHORT).show()}))
                        }
                    }


                    override fun onError(e: Throwable) {
                        if(e.message!!.contains("empty")){
                            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(context, "Add to cart success", Toast.LENGTH_SHORT).show()
                                    //Sdelat schetchik, esli budet vremia(14)
                                },{t:Throwable? -> Toast.makeText(context, "[Insert cart]"+t!!.message,Toast.LENGTH_SHORT).show()}))
                        }else
                            Toast.makeText(context, "[Cart Err]"+e.message, Toast.LENGTH_SHORT).show()
                    }

                })

        }

        }
    //Displaying all available addons
    private fun displayAllAddon() {
        if (Common.foodSelected!!.addon!!.size > 0){
            chip_group_addon!!.clearCheck()
            chip_group_addon!!.removeAllViews()

            edt_search_addon!!.addTextChangedListener(this)

            for (addonModel in Common.foodSelected!!.addon){

                    val chip = layoutInflater.inflate(R.layout.layout_chip, null,false) as Chip
                    chip.text = StringBuilder(addonModel.name!!).append("(+").append(addonModel.price).append(" zł").append(")").toString()
                    chip.setOnCheckedChangeListener { compoundButton, b ->
                        if (b){
                            if (Common.foodSelected!!.userSelectedAddon == null)
                                Common.foodSelected!!.userSelectedAddon = ArrayList()
                            Common.foodSelected!!.userSelectedAddon!!.add(addonModel)
                        }
                    }
                    chip_group_addon!!.addView(chip)

            }
        }
    }
    //Displaying checked addons
    private fun displayUserSelectedAddon() {
        if (Common.foodSelected!!.userSelectedAddon != null && Common.foodSelected!!.userSelectedAddon!!.size > 0)
        {
            chip_group_user_selected_addon!!.removeAllViews()
            for(addonModel in Common.foodSelected!!.userSelectedAddon!!){
                val chip = layoutInflater.inflate(R.layout.layout_chip_with_delete, null, false) as Chip
                chip.text = StringBuilder(addonModel.name!!).append("(+").append(addonModel.price).append(" zł").append(")").toString()
                chip.isClickable = false
                chip.setOnCloseIconClickListener { view ->
                    chip_group_user_selected_addon!!.removeView(view)
                    Common.foodSelected!!.userSelectedAddon!!.remove(addonModel)
                    calculateTotalPrice()
                }
                chip_group_user_selected_addon!!.addView(chip)

            }
        }else if (Common.foodSelected!!.userSelectedAddon!!.size == 0)
            chip_group_user_selected_addon!!.removeAllViews()
    }


    /*private fun showDialogRating() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Food Rating")
        builder.setMessage("Please enter tour comment")

        val itemView  = LayoutInflater.from(context).inflate(R.layout.layout_rating_comment,null)

        val ratingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
        val edt_comment = itemView.findViewById<EditText>(R.id.edt_comment)

        builder.setView(itemView)

        builder.setNegativeButton("Cancel"){dialogInterface, i -> dialogInterface.dismiss()}

        builder.setPositiveButton("Ok"){dialogInterface, i ->
            val commentModel = CommentModel()
            //commentModel.name = currentUser!!.name
            //commentModel.uid = currentUser!!.uid
            commentModel.comment = edt_comment.text.toString()
            commentModel.ratingValue = ratingBar.rating
            val serverTimeStamp = HashMap<String,Any>()
            serverTimeStamp["timeStamp"] = ServerValue.TIMESTAMP
            commentModel.commentTimeStamp = (serverTimeStamp)

            foodDetailViewModel.setCommentModel(commentModel)
        }

        val dialog = builder.create()
        dialog.show()


    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        TODO("Not yet implemented")
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        chip_group_addon!!.clearCheck()
        chip_group_addon!!.removeAllViews()
        for (addonModel in Common.foodSelected!!.addon){
            if (addonModel.name!!.toLowerCase().contains(charSequence.toString().toLowerCase())){
                val chip = layoutInflater.inflate(R.layout.layout_chip, null,false) as Chip
                chip.text = StringBuilder(addonModel.name!!).append("(+ zl").append(addonModel.price).append(")").toString()
                chip.setOnCheckedChangeListener { compoundButton, b ->
                    if (b){
                        if (Common.foodSelected!!.userSelectedAddon == null)
                            Common.foodSelected!!.userSelectedAddon = ArrayList()
                        Common.foodSelected!!.userSelectedAddon!!.add(addonModel)
                    }
                }
                chip_group_addon!!.addView(chip)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        TODO("Not yet implemented")
    }
}


