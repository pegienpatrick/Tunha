package com.tunha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class CreateAccountDoctor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_doctor)

        var login=findViewById<Button>(R.id.login)
        login.setOnClickListener(View.OnClickListener {
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        })
    }


}