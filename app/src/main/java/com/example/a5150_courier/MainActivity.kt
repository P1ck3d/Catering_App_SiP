@file:Suppress("DEPRECATION")

package com.example.a5150_courier


import android.accounts.Account
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.GnssAntennaInfo.Listener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.a5150_courier.Common.Common.USER_REFERENCE
import com.example.a5150_courier.Model.UserModel
import com.example.a5150_courier.LogInAct
import com.example.a5150_courier.R.*
import com.example.a5150_courier.R.id.edt_name
import com.example.a5150_courier.R.id.log_out
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dmax.dialog.SpotsDialog
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.handleCoroutineException
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listener: FirebaseAuth.AuthStateListener
    lateinit var providers: List<AuthUI.IdpConfig>
    final val AUTH_REQUEST_CODE = 5150

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(listener)
    }

    override fun onStop() {
        firebaseAuth.removeAuthStateListener(listener)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        init()
    }


        //override fun onCreate(savedInstanceState: Bundle?) {
            //super.onCreate(savedInstanceState)
            //setContentView(R.layout.activity_log_in)

            //init()
       //}

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
            fun getUserProfile() {
                // [START get_user_profile]
                val user = Firebase.auth.currentUser
                user?.let {
                    // Name, email address, and profile photo Url
                    val name = user.displayName
                    val email = user.email
                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    val uid = user.uid
                }
                // [END get_user_profile]
            }

        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.main_menu, menu)
            return super.onCreateOptionsMenu(menu)
        }

        fun signOut() {
            // [START auth_fui_signout]
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                }// ...
        }

        // [END auth_fui_signout]
        /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == R.id.log_out) {
                signOut()
                //Firebase.auth.signOut()
                //finish()
            }
            return super.onOptionsItemSelected(item)
        }
    private fun goToHomeActvity(userModel: UserModel?) {
        com.example.a5150_courier.Common.Common.currentUser = userModel!!
        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        finish()
    }*/


}


    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.log_out){

            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
*/

/*class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listener: FirebaseAuth.AuthStateListener
    private lateinit var dialog: AlertDialog
    private val compositeDisposable = CompositeDisposable()


    private lateinit var userRef:DatabaseReference
    private var providers:List<AuthUI.IdpConfig>? = null

    companion object{
        private val APP_REQUEST_CODE = 5150
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener (listener)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onStop() {
        if (listener != null)
        firebaseAuth.removeAuthStateListener(listener)
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = Firebase.database("https://courier-8bf5f-default-rtdb.europe-west1.firebasedatabase.app")
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        init()
    }
    private fun init(){
        providers = Arrays.asList<AuthUI.IdpConfig>(AuthUI.IdpConfig.PhoneBuilder().build())
        userRef = FirebaseDatabase.getInstance().getReference(com.example.a5150_courier.Common.Common.USER_REFERENCE)
        firebaseAuth = FirebaseAuth.getInstance()
        //dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        listener = FirebaseAuth.AuthStateListener { FirebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user !=null) {
                checkUserFromFirebase(user!!)
            } else {
                phoneLogin()
            }
        }
    }



    private fun checkUserFromFirebase(user: FirebaseUser){
        dialog!!.show()
        userRef!!.child(user!!.uid)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@MainActivity,""+p0.message,Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                    val userModel = p0.getValue(UserModel::class.java)
                    goToHomeActivity(userModel)
                } else {
                   showRegistrationDialog(user!!)
                }
                
                dialog!!.dismiss()
            }

            })


    }

    @SuppressLint("MissingInflatedId")
    private fun showRegistrationDialog(user: FirebaseUser) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("REGISTER")
        builder.setMessage("Please enter information")

        val itemView = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.reg_layout, null)

        val edt_name = itemView.findViewById<EditText>(R.id.edt_name)//происзодит поиск по id кнопок
        val edt_adress = itemView.findViewById<EditText>(R.id.edt_address)
        val edt_phone = itemView.findViewById<EditText>(R.id.edt_phone)

        edt_phone.setText(user!!.phoneNumber)

        builder.setView(itemView)
        builder.setNegativeButton("CANCEL"){dialogInterface, i-> dialogInterface.dismiss()}
        builder.setPositiveButton("REGISTER"){ dialogInterface, i->
            if (TextUtils.isDigitsOnly(edt_name.text.toString())) {
                Toast.makeText(this@MainActivity,"Enter your name",Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            else if (TextUtils.isDigitsOnly(edt_adress.text.toString())) {
                Toast.makeText(this@MainActivity,"Enter your address",Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            val userModel = UserModel()
            userModel.uid = user!!.uid
            userModel.name = edt_name.text.toString()
            userModel.address = edt_adress.text.toString()
            userModel.phone = edt_phone.text.toString()

            userRef!!.child(user!!.uid)
                .setValue(userModel)
                .addOnCompleteListener { task->
                    if(task.isSuccessful)
                    {
                        dialogInterface.dismiss()
                        Toast.makeText(this@MainActivity,"Registration successfully completed",Toast.LENGTH_SHORT).show()
                        goToHomeActivity(userModel)
                    }
                }
        }
    }

    private fun goToHomeActivity(userModel: UserModel?) {
        com.example.a5150_courier.Common.Common.currentUser = userModel!!
        startActivity(Intent(this@MainActivity, HomeActvity::class.java))

    }

    private fun phoneLogin() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers!!).build(), APP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
            }

        }
    }
}*/
    /*final val AUTH_REQUEST_CODE = 5150
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var listener:FirebaseAuth.AuthStateListener
    lateinit var providers:List<AuthUI.IdpConfig>

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener (listener)
    }

    override fun onStop() {
        firebaseAuth.removeAuthStateListener(listener)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){
        providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        firebaseAuth = FirebaseAuth.getInstance()
        listener = object:FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = p0.currentUser
                if(user != null){
                }
                else{
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                    startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.cw_logo_1_)
                        .setTheme(R.style.LoginTheme)
                        .build(),AUTH_REQUEST_CODE)
                }
            }
        }
    }
    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }*/



