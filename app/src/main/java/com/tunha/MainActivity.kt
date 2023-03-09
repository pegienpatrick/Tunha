package com.tunha

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        var adm=Admin("pats@gmail.com","Patrick G","123")
        adm.setId("1")
        adm.addToFirebase()


        checkValidation();


    }

    fun checkValidation(){
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        // Add click listener to login button
        loginButton.setOnClickListener {
            Log.d(TAG, "checkValidation: ")
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val database = Firebase.database
            val myRef = database.getReference("users")

// Query data where the "email" field is equal to "test@example.com"
            val query = myRef.orderByChild("email")
            query.addValueEventListener(object :
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
                                    Toast.makeText(applicationContext,"Login Successful: "+emailEditText.text.toString(),Toast.LENGTH_SHORT).show()
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