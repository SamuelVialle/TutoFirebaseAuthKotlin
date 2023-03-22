package com.samuelvialle.tutofirebaseauthkotlin.activities.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.samuelvialle.tutofirebaseauthkotlin.R
import com.samuelvialle.tutofirebaseauthkotlin.activities.BaseActivity
import com.samuelvialle.tutofirebaseauthkotlin.firebase.AuthenticationClass

class ForgotPasswordActivity : BaseActivity() {

    // ***** GLOBAL VARS *****
    private var toolbarForgotPasswordActivity: androidx.appcompat.widget.Toolbar? = null //1 Toolbar
    private var btnSubmit: Button? = null //3 Button submit
    private var etEmail: EditText? = null //4 EditText email

    private fun initUi() {
        toolbarForgotPasswordActivity = findViewById(R.id.toolbar_forgot_password) //1.1
        btnSubmit = findViewById(R.id.btn_submit) //3.1
        etEmail = findViewById(R.id.et_email_forgot_password) //4.2
    }

    // ***** MY FUNCTIONS *****
    /** Function to setup the actionBar with back icone and without name **/ //2
    private fun setupActionBar() {
        setSupportActionBar(toolbarForgotPasswordActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24)
        }
        toolbarForgotPasswordActivity?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    //***** LIFECYCLE *****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initUi() // 1.2
        setupActionBar() //2

        btnSubmit?.setOnClickListener { //5
            var email: String = etEmail!!.text.toString().trim { it <= ' ' }

            if (email == "") {
                showSnackBar(getString(R.string.err_msg_email_empty), true)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showSnackBar(resources.getString(R.string.err_msg_email_missmatch), true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()
                        if (task.isSuccessful) {
                            Toast.makeText( // L'utilisation d'un taost est préférable à cet endroit pour permettre un affichage plus long
                                this@ForgotPasswordActivity,
                                resources.getString(R.string.email_sent_success),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            showSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }
}