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

    }




