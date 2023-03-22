package com.samuelvialle.tutofirebaseauthkotlin.customWidgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MyButton(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {
    init {
        // Appel de la fonction pour la gestion de la police du widget
        applyFont()
    }

    private fun applyFont() {
        // Récupération de la police dans assets pour l'ajout au textView
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "montserrat_bold.ttf")
        setTypeface(typeface)
    }
}