package com.tunha

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin)

        var email=findViewById<EditText>(R.id.email)
        var password=findViewById<EditText>(R.id.password)
        var fname=findViewById<EditText>(R.id.fname)

        var add=findViewById<Button>(R.id.add_button)
        add.setOnClickListener(View.OnClickListener {
            val user=Admin(email.text.toString(),fname.text.toString(),password.text.toString())
            if(user.getEmail().isEmpty()) {
                email.error="Email cant be empty"
                Toast.makeText(
                    applicationContext,
                    "Email cant be empty",
                    Toast.LENGTH_SHORT
                )
                //return@OnClickListener
            }
            else {
                User.fetchUsersFromDatabase { userList ->
                    // Do something with the user list
                    var error = false
                    for (i in userList) {
                        Log.d(TAG, " gotten user "+i.toString()+i+ i.getEmail())
                        if (i.getEmail() == user.getEmail()) {
                            email.error = "Email Already Registered"
                            Toast.makeText(
                                applicationContext,
                                "Email ${i.getEmail()} already Registered",
                                Toast.LENGTH_SHORT
                            )
                            error = true
                            break

                        }
                    }
                    if (!error) {
                        user.addToFirebase()
                        finish()
                        Log.d(TAG,"Expected Data Here ")
                    }
                }

            }
        })
        var canc=findViewById<Button>(R.id.cancel)
        canc.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}