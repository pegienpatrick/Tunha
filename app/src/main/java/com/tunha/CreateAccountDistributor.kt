package com.tunha

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class CreateAccountDistributor : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_distributor)

        var login=findViewById<Button>(R.id.login)
        login.setOnClickListener(View.OnClickListener {
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        })


        var uploadButton = findViewById<Button>(R.id.upload_button)
        uploadButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }
    }
}