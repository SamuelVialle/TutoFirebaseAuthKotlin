package com.samuelvialle.tutofirebaseauthkotlin.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.samuelvialle.tutofirebaseauthkotlin.activities.BaseActivity
import com.samuelvialle.tutofirebaseauthkotlin.activities.login.LoginActivity
import com.samuelvialle.tutofirebaseauthkotlin.activities.login.RegisterActivity
import com.samuelvialle.tutofirebaseauthkotlin.models.UserDetail
import com.samuelvialle.tutofirebaseauthkotlin.utils.Constants

class FirestoreClass {

    // Initialisation de l'instance de Firestore
    private val mFirestore = FirebaseFirestore.getInstance()

    /** Function used to upload data into the Firestore database using a merge based on userDetail model **/
    fun registerUser(activity: RegisterActivity, userDetail: UserDetail) {
        mFirestore.collection(Constants.USERS)// Users est le nom de la collection. Si la collection n'existe pas elle est créée automatiquement
            .document(userDetail.id)// Here the document is the User ID from Authentication
            .set(userDetail, SetOptions.merge()) // Merge instead of replacing the field
            .addOnSuccessListener {
                activity.userRegistrationSuccess() // Cette méthode est dans RegisterActivity
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while registering the user", e)
            }
    }

    /** Function to get the user details from firestore using the currentUserID, notice that function use
     * Activity so it can be call from multiple activities
     */
    fun getUserDetails(activity: Activity) {
        mFirestore.collection(Constants.USERS)
            .document(AuthenticationClass().getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i("TAG", "getUserDetails: $document")
                // Here we have received the document snapshot and convert it into an object based on the UserDetail model
                val userDetail = document.toObject(UserDetail::class.java)!!

                when (activity) {
                    is LoginActivity -> {
                        // Call a function a function af base activity for transferring the result to it
                        activity.userLoggedInSuccess(userDetail)
                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

}