package com.samuelvialle.tutofirebaseauthkotlin.activities

import android.app.Activity
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.samuelvialle.tutofirebaseauthkotlin.R

open class BaseActivity : AppCompatActivity() {

    // Suppression du OnCreate !!! On utilise celui de chacune des activités
    /** Méthode pour l'affichage plein écran **/
    fun fullScreen(onOff: Boolean){
        if(onOff){
            @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        }
    }

    /** Méthode pour la gestion de la snackbar **/
    fun showSnackBar(message: String, errorMessage: Boolean) {
        val snackbar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.color_snackBar_error)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.color_snackBar_success)
            )
        }
        snackbar.show()
    }

    /** Méthode pour afficher La progressDialog **/
    // Initialisation de la progressDialog
    private lateinit var mProgressDialog: Dialog
    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)
        /* Set the screen content from a layput resource.
            The resource wil be inflated, adding all top-level views to the screen. */
        // Association du layout personnalisé
        mProgressDialog.setContentView(R.layout.custom_progress_dialog)
        // Ajout de la possibilité de modifier le texte à la volet depuis l'appel de la méthode
        var tvProgressDialog: TextView = mProgressDialog.findViewById(R.id.tv_progress_text)
        tvProgressDialog.text = text
        // Ajout des actions en cas d'annulation
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        // Démarrage de l'affichage sur les écrans
        mProgressDialog.show()
        //TODO CHANGE mProgressDialog for an alert dialog ??
    }
    /** Méthode pour cacher La progressDialog **/
    // Comme l'annulation n'est pas disponible et qu'aucune action pour quitter n'existe il faut une fonction pour.
    fun hideProgressDialog(){
            mProgressDialog.dismiss()
    }

    /** Méthode pour la gestion des comportements claviers **/
    fun EditText.onAction(action: Int, runAction: () -> Unit) {
        this.setOnEditorActionListener { view, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                action -> {
                    runAction.invoke()
                    true
                }
                else -> false
            }
        }
    }

    /** Méthode pour l'affichage des logs' **/

    fun myLogi(message: String){
        var TAG: String = this.localClassName
        Log.i(TAG, message)
    }


}