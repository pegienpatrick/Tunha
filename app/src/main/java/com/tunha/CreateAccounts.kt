package com.tunha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class CreateAccounts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_accounts)

        var login=findViewById<Button>(R.id.login)
        login.setOnClickListener(View.OnClickListener {
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        })

        var doctorLogin = findViewById<LinearLayout>(R.id.doctorLoginButton)
        doctorLogin.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, CreateAccountDoctor::class.java)
            startActivity(intent)

        })


        var distributorLogin= findViewById<LinearLayout>(R.id.distributorLoginButton)
        distributorLogin.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, CreateAccountDistributor::class.java)
            startActivity(intent)

        })


    }
}