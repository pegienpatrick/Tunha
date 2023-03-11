package com.tunha

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tunha.Session.Companion.saveUserSession

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

//        var adm=Admin("pats@gmail.com","Patrick G","123")
//        adm.setId("1")
//        adm.addToFirebase()




        checkValidation();

        var ca:Button=findViewById(R.id.cancel)
        ca.setOnClickListener(View.OnClickListener {
            var intent:Intent=Intent(this,CreateAccounts::class.java)
            startActivity(intent)
        })


    }

    fun checkValidation(){
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button);

        run {
            emailEditText.setText("pats@gmail.com")
            passwordEditText.setText("123")

        }

        // Add click listener to login button
        loginButton.setOnClickListener {
            Log.d(TAG, "checkValidation: ")
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val database = Firebase.database
            val myRef = database.getReference("users")

// Query data where the "email" field is equal to "test@example.com"
            val query = myRef.orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object :
                ValueEventListener {
                
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange: ")
                    var got:Boolean=false
                    for (childSnapshot in dataSnapshot.children) {
                        Log.d(TAG,"reading dta")
                        // Print the data for each child node
                        println("${childSnapshot.key} => ${childSnapshot.value}")
                        val us: User? = childSnapshot.getValue(User::class.java)
                        if (us != null) {
                            if(us.getEmail() == email){
                                got=true
                                if(us.login(password)) {
                                    var s=Session()

                                    saveUserSession(applicationContext,us.getId(),us.getUserType())
                                    Toast.makeText(applicationContext,"Login Successful: "+emailEditText.text.toString(),Toast.LENGTH_SHORT).show()
                                    if(us.getUserType() == "Admin") {
                                        var intent =
                                            Intent(applicationContext, AdminAccount::class.java)
                                        startActivity(intent)
                                    }
                                    else{
                                        Log.d(TAG,"User type : "+us.getUserType())
                                    }

                                } else {
                                    Toast.makeText(applicationContext,"invalid Login : "+emailEditText.text.toString(),Toast.LENGTH_SHORT).show()
                                }

                            }
                        }

                    }
                    if(!got)
                        Toast.makeText(applicationContext,"Email not registered : "+emailEditText.text.toString(),Toast.LENGTH_LONG).show()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext,"Email not registered : "+emailEditText.text.toString(),Toast.LENGTH_LONG).show()
                    //println("Error getting data: ${error.message}")
                }
            })
        }
    }
}