package com.samuelvialle.tutofirebaseauthkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private var btnLogin: Button? = null
    private var btnRegister: TextView? = null
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
        setContentView(R.layout.activity_login)

        initUi()

        btnRegister?.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        })

        btnLogin?.setOnClickListener(View.OnClickListener {
            val email: String = etMail?.text.toString().trim { it <= ' ' }
            val password: String = etPassword?.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.you_are_successfully_logged),
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("userId", FirebaseAuth.getInstance().currentUser!!.uid)
                        intent.putExtra("userMail", email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        })
    }
}