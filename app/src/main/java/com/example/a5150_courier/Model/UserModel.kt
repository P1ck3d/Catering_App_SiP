package com.example.a5150_courier.Model

import com.example.a5150_courier.HomeActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserModel {
    var uid = Firebase.auth.currentUser!!.uid
    var name = Firebase.auth.currentUser!!.displayName



}