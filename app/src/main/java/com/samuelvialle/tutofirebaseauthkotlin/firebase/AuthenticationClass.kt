package com.samuelvialle.tutofirebaseauthkotlin.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

open class AuthenticationClass {

    private val mAuth = FirebaseAuth.getInstance()

    /** Function to get the currentUser Id from authenticator **/
    fun getCurrentUserID(): String{
        val currentUser = mAuth.currentUser

        // A variable to assigne the currentUserID if it is not null or else it will be blank
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    /** Function to check if an account with the same mail used for register or for send reset password mail
     * already exist in Authentication
     */
    fun checkIfUserAlreadyExist(email: String){
        mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener{ task ->
                var isNewUser: Boolean = task.getResult().signInMethods!!.isEmpty()

                if(isNewUser){
                    Log.i("TAG", "NEW")
                } else {
                    Log.i("TAG", "OLD")
                }
            }
    }
}