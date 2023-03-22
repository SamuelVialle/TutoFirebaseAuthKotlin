package com.samuelvialle.tutofirebaseauthkotlin.activities.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.samuelvialle.tutofirebaseauthkotlin.R

class SplashActivity : AppCompatActivity() {

//    // Variables globales
//    private var tv_AppName: TextView? = null
//
//    // Méthode d'initialisation des composants
//    private fun initUI(){
//        tv_AppName = findViewById(R.id.tv_app_name)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 0 InitUI ;)
//        initUI()

        // 1 Add fullscreen
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        // 2 Add Handler for the delay
        // we used the postDelayed(Runnable, time) method to send a message with a delayed time.
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500) // 1500 is the delayed time in milliseconds.

        // 3 Gestion de l'ajout de la police au titre
//        val typeface: Typeface = Typeface.createFromAsset(assets, "montserrat_bold.xml")
//        var tv_AppName = findViewById<TextView>(R.id.tv_app_name)
//        tv_AppName.typeface = typeface


    }


}

