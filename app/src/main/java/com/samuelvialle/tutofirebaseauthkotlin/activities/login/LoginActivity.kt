package com.samuelvialle.tutofirebaseauthkotlin.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.samuelvialle.tutofirebaseauthkotlin.R
import com.samuelvialle.tutofirebaseauthkotlin.activities.BaseActivity
import com.samuelvialle.tutofirebaseauthkotlin.activities.MainActivity
import com.samuelvialle.tutofirebaseauthkotlin.firebase.FirestoreClass
import com.samuelvialle.tutofirebaseauthkotlin.models.UserDetail

class LoginActivity : BaseActivity(), View.OnClickListener {

    private var btnLogin: Button? = null
    private var btnRegister: TextView? = null
    private var btnForgotPassword: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null


    fun initUi() {
        btnRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)
        btnForgotPassword = findViewById(R.id.btn_forgot_password)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUi()

        // Méthodes pour la gestion des clics basé sur l'utilisation d'objets Java liés au design et
        // l'utilisation de setOnclickListener. Dans un second temps ces méthodes sont laissées au profit
        // de l'implémentation de View.OnClickListener et les actions sont lancées de puis l'override de la méthode onClick
//        btnRegister?.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
//        })
//
//        btnLogin?.setOnClickListener(View.OnClickListener {
//            loginUser()
//        })
//
        btnLogin?.setOnClickListener(this)
        btnRegister?.setOnClickListener(this)
        btnForgotPassword?.setOnClickListener(this)

        // Ajout de la méthode pour ma gestion des touches du clavier dans BaseActivity pour une utilisation ici
        etPassword?.onAction(EditorInfo.IME_ACTION_DONE) {
            logInRegisteredUser()
        }
    }

    // Méthode pour l'enregistrement encapsulée pour être utilisée dans le clickListener et la méthode de touches du clavier
    fun logInRegisteredUser() {
        if (validateloginDetail()) {
            // Affichage progressDialog
            showProgressDialog(resources.getString(R.string.please_wait))

            // Récupération des valeurs
            val email: String = etEmail?.text.toString().trim { it <= ' ' }
            val password: String = etPassword?.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()
                        // Dans un premier temps l'utilisation du toast est à favoriser
                        showSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    /** Function to validate data from the login form **/
    private fun validateloginDetail(): Boolean {
        var email: String? = etEmail!!.text.toString().trim { it <= ' ' }
        return when {
            TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showSnackBar(getString(R.string.err_msg_email_empty), true)
                false
            }
            TextUtils.isEmpty(
                etPassword!!.text.toString().trim { it <= ' ' }) || etPassword!!.length() <= 7 -> {
                showSnackBar(getString(R.string.err_msg_password), true)
                false
            }
            else -> {
//                showErrorSnackBar(getString(R.string.registery_successfull), false)
                // On ne place plus rien ici le message sera affiché dans loginUser()
                true
            }
        }
    }

    /** Function use to make clickable Login Button, ForgotPassword and Register TextView **/
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_forgot_password -> {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
            R.id.btn_register -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            R.id.btn_login -> {
                logInRegisteredUser()
            }
        }
    }

    /** Function to get data from user detail when user is logged **/
    fun userLoggedInSuccess(userDetail: UserDetail){
        hideProgressDialog()

        myLogi("First Name is: ${userDetail.firstName}")
        myLogi("Last Name is: ${userDetail.lastName}")
        myLogi("Email is: ${userDetail.email}")

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}