package com.samuelvialle.tutofirebaseauthkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.samuelvialle.tutofirebaseauthkotlin.R
import com.samuelvialle.tutofirebaseauthkotlin.activities.login.LoginActivity

class MainActivity : BaseActivity() {

    private var btnLogout: Button? = null
    private var tvUserId: TextView? = null
    private var tvUserMail: TextView? = null

    fun initUi(){
        btnLogout=findViewById(R.id.btn_logout)
        tvUserId=findViewById(R.id.tv_userId)
        tvUserMail=findViewById(R.id.tv_user_mail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUi()

        val userId = intent.getStringExtra("userId")
        val userMail = intent.getStringExtra("userMail")

        tvUserId?.text = "User ID: $userId"
        tvUserMail?.text = "Mail user: $userMail"

        btnLogout?.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        })
    }
}