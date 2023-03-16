package com.samuelvialle.tutofirebaseauthkotlin.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.samuelvialle.tutofirebaseauthkotlin.RegisterActivity
import com.samuelvialle.tutofirebaseauthkotlin.models.User

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        mFirestore.collection("Users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess() // Cette méthode est dans RegisterActivity
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "registerUser: ", e)
            }
    }

}