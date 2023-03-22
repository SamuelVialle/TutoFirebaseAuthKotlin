package com.samuelvialle.tutofirebaseauthkotlin.customWidgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MyTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs){
    init {
        // Appel de la fonction pour la gestion de la police du widget
        applyFont()
    }

    private fun applyFont() {
        // Récupération de la police dans le dossier assets pour l'ajout au textView
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "montserrat.ttf")
        setTypeface(typeface)
    }
}