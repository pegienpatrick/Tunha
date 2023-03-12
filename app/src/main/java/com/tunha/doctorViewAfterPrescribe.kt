package com.tunha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class doctorViewAfterPrescribe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_view_after_prescribe)

        val backButton = findViewById<ImageView>(R.id.backbutton)
        backButton.setOnClickListener {
            finish()
        }
    }
}