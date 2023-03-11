package com.tunha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class chemistDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chemist_details)


        val backButton = findViewById<ImageView>(R.id.backbutton)
        backButton.setOnClickListener {
            finish()
        }

    }
}