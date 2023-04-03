/*package com.example.a5150_courier

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.a5150_courier.EventBus.CategoryClick
import com.example.a5150_courier.databinding.ActivityHomeBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listener: FirebaseAuth.AuthStateListener
    lateinit var providers: List<AuthUI.IdpConfig>
    final val AUTH_REQUEST_CODE = 5150
    //Auth


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(listener)
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        firebaseAuth.removeAuthStateListener(listener)
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        binding.appBarHome.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_menu, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        init()
    }

    private fun init() {
        providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        firebaseAuth = FirebaseAuth.getInstance()
        listener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = p0.currentUser
                if (user != null) {
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                    startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.cw_logo_1_)
                            .setTheme(R.style.LoginTheme)
                            .build(), AUTH_REQUEST_CODE
                    )
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //EventBus
    //override fun onStart(){
       // super.onStart()

    //}
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCategorySelected(event:CategoryClick){
        if(event.isSuccess){
            Toast.makeText(this,"Click to"+event.category.name,Toast.LENGTH_SHORT).show()
        }
    }

}*/
package com.example.a5150_courier

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.a5150_courier.Common.Common
import com.example.a5150_courier.EventBus.*
import com.example.a5150_courier.Model.CategoryModel
import com.example.a5150_courier.Model.FoodModel
import com.example.a5150_courier.Model.PopularCategoryModel
import com.example.a5150_courier.databinding.ActivityHomeBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*


class HomeActivity : AppCompatActivity() {
    //Auth
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listener: FirebaseAuth.AuthStateListener
    lateinit var providers: List<AuthUI.IdpConfig>
    lateinit var userRef:DatabaseReference
    final val AUTH_REQUEST_CODE = 5150

    private lateinit var navController: NavController
    private lateinit var dialog:AlertDialog




    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding


    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(listener)
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        firebaseAuth.removeAuthStateListener(listener)
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarHome.toolbar)

        binding.appBarHome.fab.setOnClickListener { view ->
            //val navController = findNavController(R.id.nav_cart)
            navController.navigate(R.id.nav_cart)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_menu, R.id.nav_food_detail,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(object:NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                item.isCheckable = true
                drawerLayout.closeDrawers()
                if(item.itemId == R.id.nav_log_out){
                signOut()
                }else if (item.itemId == R.id.nav_home){
                    navController.navigate(R.id.nav_home)
                }else if (item.itemId == R.id.nav_menu){
                    navController.navigate(R.id.nav_menu)
                }
                return true
            }
        })

        init()
    }

    private fun init() {


        providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCE)
        firebaseAuth = FirebaseAuth.getInstance()
        listener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = p0.currentUser
                if (user != null) {
                    checkUserFromFirebase(user)

                } else {
                    startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.cw_logo_1_)
                            .setTheme(R.style.LoginTheme)
                            .build(), AUTH_REQUEST_CODE
                    )
                }
            }
        }

    }

    private fun checkUserFromFirebase(user: FirebaseUser) {
        userRef!!.child(user!!.uid)
            //.setValue(userModel)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onHideFab(event: HideFab) {
        if (event.isHide) {
            binding.appBarHome.fab.hide()
        }else{
            binding.appBarHome.fab.show()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCategorySelected(event: CategoryClick) {
        if (event.isSuccess) {
            findNavController(R.id.nav_host_fragment_content_home).navigate(R.id.nav_food_list)
            //Toast.makeText(this,"Click to"+event.category.name,Toast.LENGTH_SHORT).show() delau debug
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onFoodSelected(event: FoodItemClick) {
        if (event.isSuccess) {
            findNavController(R.id.nav_host_fragment_content_home).navigate(R.id.nav_food_detail)


        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onPopularClick(event:PopularFoodClick){
        if (event.popularCategoryModel != null){
            FirebaseDatabase.getInstance()
                .getReference("Category")
                .child(event.popularCategoryModel.menu_id!!)
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        dialog!!.dismiss()
                        Toast.makeText(this@HomeActivity,""+error.message,Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            Common.categorySelected = snapshot.getValue(CategoryModel::class.java)

                            //Food is loading
                            FirebaseDatabase.getInstance()
                                .getReference("Category")
                                .child(event.popularCategoryModel.menu_id!!)
                                .child("foods")
                                .orderByChild("id")
                                .equalTo(event.popularCategoryModel.food_id)
                                .limitToLast(1)
                                .addListenerForSingleValueEvent(object:ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.exists()){
                                            for (foodSnapShot in snapshot.children)
                                                Common.foodSelected = foodSnapShot.getValue(FoodModel::class.java)
                                            navController.navigate(R.id.nav_food_detail)

                                        }else{
                                            //dialog!!.dismiss()
                                            Toast.makeText(this@HomeActivity,"Niestety",Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        dialog!!.dismiss()
                                        Toast.makeText(this@HomeActivity,""+error.message,Toast.LENGTH_SHORT).show()
                                    }

                                })
                        }else{
                            dialog!!.dismiss()
                            Toast.makeText(this@HomeActivity,"Niestety",Toast.LENGTH_SHORT).show()
                        }
                    }
                })

        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onBestClick(event:BestDealClick){
        if (event.model != null){
            FirebaseDatabase.getInstance()
                .getReference("Category")
                .child(event.model.menu_id!!)
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        dialog.dismiss()
                        Toast.makeText(this@HomeActivity,""+error.message,Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            Common.categorySelected = snapshot.getValue(CategoryModel::class.java)

                            //Food is loading
                            FirebaseDatabase.getInstance()
                                .getReference("Category")
                                .child(event.model.menu_id!!)
                                .child("foods")
                                .orderByChild("id")
                                .equalTo(event.model.food_id)
                                .limitToLast(1)
                                .addListenerForSingleValueEvent(object:ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.exists()){
                                            for (foodSnapShot in snapshot.children)
                                                Common.foodSelected = foodSnapShot.getValue(FoodModel::class.java)
                                            navController.navigate(R.id.nav_food_detail)

                                        }else{
                                            //dialog!!.dismiss()
                                            Toast.makeText(this@HomeActivity,"Niestety",Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        dialog!!.dismiss()
                                        Toast.makeText(this@HomeActivity,""+error.message,Toast.LENGTH_SHORT).show()
                                    }

                                })
                        }else{
                            dialog!!.dismiss()
                            Toast.makeText(this@HomeActivity,"Niestety",Toast.LENGTH_SHORT).show()
                        }
                    }
                })

        }
    }


    private fun signOut() {
        // [START auth_fui_signout]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Wylogowanie")
            .setMessage("Wylogować z konta?")
            .setNegativeButton("Nie") { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton("Tak") { dialogInterface, _ ->
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                    }

                /*val intent = Intent(this@HomeActivity,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()*/
            }
        val dialog = builder.create()
        dialog.show()
    }

}