package com.tunha

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.OpenableColumns.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tunha.Session.Companion.saveUserSession
import com.tunha.ui.doctors.DoctorAccount

class CreateAccountDoctor : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 123
    private var cert:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_doctor)

        var login=findViewById<Button>(R.id.login)
        login.setOnClickListener(View.OnClickListener {
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        })


        var uploadButton = findViewById<Button>(R.id.upload_button)
        uploadButton.setOnClickListener {
            val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, do something
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            } else {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
            }


        }

        var email=findViewById<EditText>(R.id.email)
        var password=findViewById<EditText>(R.id.password)
        var fname=findViewById<EditText>(R.id.fname)
        var medpin=findViewById<EditText>(R.id.medpin)

        var add=findViewById<Button>(R.id.buttonSubmit)
        add.setOnClickListener(View.OnClickListener {
            val user=Doctor(email.text.toString(),fname.text.toString(),password.text.toString(),medpin.text.toString())
            if(user.getMedicalPin().isEmpty())
            {
                medpin.error="Medical Pin cant be empty"
                Toast.makeText(
                    applicationContext,
                    "Medical Pin cant be empty",
                    Toast.LENGTH_SHORT
                )

            }
            else if(cert==null)
            {
                Toast.makeText(
                    applicationContext,
                    "You need to upload your Certificate",
                    Toast.LENGTH_SHORT
                )
            }
            else if(user.getEmail().isEmpty()) {
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
                        Log.d(ContentValues.TAG, " gotten user "+i.toString()+i+ i.getEmail())
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
                        user.setCertImage(applicationContext, cert!!)
                        //finish()
                        saveUserSession(applicationContext,user.getId(),user.getUserType())
                        var intent =
                                            Intent(applicationContext, DoctorAccount::class.java)
                                        startActivity(intent)
                        Log.d(ContentValues.TAG,"Expected Data Here ")
                    }
                }

            }
        })



    }

    // Override the onActivityResult method to retrieve the selected image URI and file name
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data // retrieve the selected image URI

            // Retrieve the file name associated with the selected image URI
            val contentResolver = applicationContext.contentResolver
            val cursor = selectedImageUri?.let { contentResolver.query(it, null, null, null, null) }
            cursor?.let {
                if (it.moveToFirst()) {
                    val fileName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                    // do something with the file name, such as displaying it to the user
                    val celfile=findViewById<TextView>(R.id.selfile)
                    celfile.text = fileName
                }
                cursor.close()
            }

            // Load the image into a Bitmap using an InputStream
            val inputStream = selectedImageUri?.let { contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)

            cert=bitmap
            // do something with the Bitmap, such as displaying it in an ImageView
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the grantResults array is empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, do something
                } else {
                    // Permission is denied, handle the denied permission
                }
                return
            }
            else -> {
                // Handle other permission requests
            }
        }
    }









}

