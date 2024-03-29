package com.tunha

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.tunha.User.Companion.fetchUserByIdFromDatabase

class doctorDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_details)

        val backButton = findViewById<ImageView>(R.id.backbutton)
        backButton.setOnClickListener {
            finish()
        }
        var i=intent
        if (i.hasExtra("userId")) {
            var userid = i.getStringExtra("userId")

            if (userid != null) {
                feedData(userid)
            }
        }
        else
        {
            finish()
        }






    }


    fun feedData(userid:String){


        val doctorImageView: ImageView = findViewById(R.id.dp)
        val firstNameTextView: TextView = findViewById(R.id.fname)
        val emailTextView: TextView = findViewById(R.id.email)
        val medicalPinTextView: TextView = findViewById(R.id.medpin)
        val statusTextView: TextView = findViewById(R.id.status)
        val doctorCertificateImageView: ImageView = findViewById(R.id.cert)
        val approveButton: Button = findViewById(R.id.action_button)

        val paddingValue=20
        firstNameTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        emailTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        medicalPinTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        statusTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

        fetchUserByIdFromDatabase(userid) { res ->

            if (res != null) {
                var user=res as Doctor
                firstNameTextView.text=user.getFullName()
                emailTextView.text=user.getEmail()
                medicalPinTextView.text=user.getMedicalPin()
                statusTextView.text = if (user.isApproved()) "Approved" else "Not Approved"

                user.getProfileImage(applicationContext,doctorImageView)
                user.getCertImage(applicationContext,doctorCertificateImageView)

                if(user.isApproved())
                {
                    approveButton.text=("DisApprove");
                    approveButton.setBackgroundColor(Color.RED)
                }
                else{
                    approveButton.text=("Approve");
                    approveButton.setBackgroundColor(Color.BLUE)
                }

                approveButton.setOnClickListener(View.OnClickListener {

                    val message = if (user.isApproved()) {
                        "Are you sure you want to disapprove this Doctor?"
                    } else {
                        "Are you sure you want to approve this Doctor?"
                    }
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Confirm")
                    alertDialogBuilder.setMessage(message)
                    alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                        // Set user approval status to true
                        user.setApproved(!user.isApproved())
                        user.addToFirebase()
                        feedData(userid)
                    }
                    alertDialogBuilder.setNegativeButton("No") { _, _ ->
                        // Do nothing
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                })

            }


        }


    }
}