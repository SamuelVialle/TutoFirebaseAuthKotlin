package com.samuelvialle.tutofirebaseauthkotlin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private var btnRegister: Button? = null
    private var btnLogin: TextView? = null
    private var etMail: TextInputEditText? = null
    private var etPassword: TextInputEditText? = null


    fun initUi() {
        btnRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)
        etMail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }

        initUi()

        btnRegister?.setOnClickListener {
            when {
                TextUtils.isEmpty(etMail.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, getString(R.string.please_enter_email), Toast.LENGTH_LONG)
                        .show()
                    Log.i("TAG", getString(R.string.please_enter_email))
                }

                TextUtils.isEmpty(etPassword.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.please_enter_password),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i("TAG", getString(R.string.please_enter_password))
                }
                else -> {
                    val email: String = etMail?.text.toString().trim { it <= ' ' }
                    val password: String = etPassword?.text.toString().trim { it <= ' ' }
                    Log.i("TAG", "email: $email --- password: $password")

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(OnCompleteListener { task ->
                            // If the registration is successfully done
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Log.i("TAG", "User created: ${firebaseUser.uid}")
                                Toast.makeText(
                                    this@RegisterActivity,
                                    getString(R.string.you_are_successfully_registed),
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("userId", firebaseUser.uid)
                                intent.putExtra("userMail", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                }

            }
        }

        btnLogin?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        })
    }
}