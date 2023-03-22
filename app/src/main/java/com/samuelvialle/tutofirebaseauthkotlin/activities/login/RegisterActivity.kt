package com.samuelvialle.tutofirebaseauthkotlin.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.samuelvialle.tutofirebaseauthkotlin.R
import com.samuelvialle.tutofirebaseauthkotlin.activities.BaseActivity
import com.samuelvialle.tutofirebaseauthkotlin.activities.MainActivity
import com.samuelvialle.tutofirebaseauthkotlin.firebase.FirestoreClass
import com.samuelvialle.tutofirebaseauthkotlin.models.UserDetail

// 3 Pour rendre actif le lien vers baseActivity penser à mettre opoen pour la classe baseActivity
class RegisterActivity : BaseActivity() {

    // 2 La toolbar
    private var toolbarRegistryActivity: androidx.appcompat.widget.Toolbar? = null

    // 3 Les widgets
    private var btnRegister: Button? = null
    private var btnLogin: TextView? = null
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var etConfirmPassword: EditText? = null
    private var cbTermsAndCondition: CheckBox? = null

    private var firstName: String = ""
    private var lastName: String = ""
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""

    /** Function to initialise java objects with widgets **/
    private fun initUi() {
        // 2.1
        toolbarRegistryActivity = findViewById(R.id.toolbar_registry_activity)
        // 3.1
        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        cbTermsAndCondition = findViewById(R.id.cb_terms_and_condition)

        btnRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)

    }

    /** Function to setup the actionBar with back icone and without name **/ //2
    private fun setupActionBar() {
        setSupportActionBar(toolbarRegistryActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_primary_color_24)
        }

        toolbarRegistryActivity?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    /** Function to validate the entries of a new user **/ // 4
    private fun validateRegisterDetail(): Boolean {
        return when {
            TextUtils.isEmpty(firstName) -> {
                showSnackBar(getString(R.string.err_msg_first_name), true)
                false
            }
            TextUtils.isEmpty(lastName) -> {
                showSnackBar(getString(R.string.err_msg_last_name), true)
                false
            }
            TextUtils.isEmpty(email) -> {
                showSnackBar(getString(R.string.err_msg_email_empty), true)
                false
            }
            !email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }!! -> {
                showSnackBar(getString(R.string.err_msg_email_missmatch), true)
                false
            }
            TextUtils.isEmpty(password) -> {
                showSnackBar(getString(R.string.err_msg_password), true)
                false
            }
            etPassword!!.length() <= 7 -> {
                showSnackBar(getString(R.string.err_msg_password_number), true)
                false
            }
            TextUtils.isEmpty(confirmPassword) -> {
                showSnackBar(getString(R.string.err_msg_confirm_password), true)
                false
            }
            password != confirmPassword -> {
                showSnackBar(getString(R.string.err_msg_password_and_confirm_password_missmatch),true)
                false
            }
            !cbTermsAndCondition!!.isChecked -> {
                showSnackBar(getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
//                showErrorSnackBar(getString(R.string.registery_successfull), false)
                true
            }
        }
    }

    /** Function to register the current user into Authentication and to store user detail in Firestore **/
    private fun registerUser() {
        if (validateRegisterDetail()) {
            // Show progressDialog
            showProgressDialog(resources.getString(R.string.please_wait)) //6

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener { task ->
                    // 6.1 On cache la ProgressDialog peut importe le succès ou non
                    // hideProgressDialog()
                    // If the registration is successfully done
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        // Ajout du contenu des cases du formulaire dans le model
                        val userDetail = UserDetail(
                            firebaseUser.uid,
                            firstName,
                            lastName,
                            email
                            )

                        FirestoreClass().registerUser(this@RegisterActivity, userDetail)

                        // Suppression au profit du Toast de user Registration
                        // showSnackBar(getString(R.string.you_are_successfully_register), false)

                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("userId", firebaseUser.uid)
                        intent.putExtra("userMail", email)
                        startActivity(intent)
                        finish()
                    } else {
                        hideProgressDialog()
                        showSnackBar(task.exception!!.message.toString(), true)
                    }
                })
        }
    }

    fun userRegistrationSuccess(){
        hideProgressDialog()

        Toast.makeText( // L'utilisation d'un taost est préférable à cet endroit pour permettre un affichage plus long
            this@RegisterActivity,
            resources.getString(R.string.you_are_successfully_register),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1 Fullscreen for register à déplacer dans BaseActivity
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
        fullScreen(true)

        initUi()
        setupActionBar()

        btnRegister?.setOnClickListener {
            firstName = etFirstName?.text.toString().trim { it <= ' ' }
            lastName = etLastName?.text.toString().trim { it <= ' ' }
            email = etEmail?.text.toString().trim { it <= ' ' }
            password = etPassword?.text.toString().trim { it <= ' ' }
            confirmPassword = etConfirmPassword?.text.toString().trim { it <= ' ' }
            registerUser()
        }

        btnLogin?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        })

        // Ajout de la méthode pour ma gestion des touches du clavier dans BaseActivity pour une utilisation ici
        etConfirmPassword?.onAction(EditorInfo.IME_ACTION_DONE) {
            registerUser()
        }
    }
}