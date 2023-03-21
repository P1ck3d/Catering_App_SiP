package com.example.a5150_courier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.a5150_courier.Common.Common.currentUser
import com.example.a5150_courier.Model.UserModel
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInAct : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listener:FirebaseAuth.AuthStateListener
    lateinit var providers:List<AuthUI.IdpConfig>
    final val AUTH_REQUEST_CODE = 5150

       // override fun onStart() {
          //  super.onStart()
           // firebaseAuth.addAuthStateListener (listener)
       // }

       // override fun onStop() {
          //  firebaseAuth.removeAuthStateListener(listener)
           // super.onStop()
        //}

       // override fun onCreate(savedInstanceState: Bundle?) {
         //   super.onCreate(savedInstanceState)
         //   setContentView(R.layout.activity_log_in)

         //   init()
       // }

      /*  private fun init(){
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
}*/
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
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if(item.itemId == R.id.log_out){
                signOut()
                //Firebase.auth.signOut()
                //finish()
            }
            return super.onOptionsItemSelected(item)}
    /*private fun goToHomeActvity(userModel: UserModel?){
        com.example.a5150_courier.Common.Common.currentUser = userModel!!
        startActivity(Intent(this@LogInAct, HomeActivity::class.java))
        finish()
    }*/
    }




