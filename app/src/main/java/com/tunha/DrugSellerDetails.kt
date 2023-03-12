package com.tunha

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class DrugSellerDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_seller_details)
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


        val dImageView: ImageView = findViewById(R.id.dp)
        val firstNameTextView: TextView = findViewById(R.id.fname)
        val emailTextView: TextView = findViewById(R.id.email)
        val licenseTextView: TextView = findViewById(R.id.licence)
        val statusTextView: TextView = findViewById(R.id.status)
        val licenseImageView: ImageView = findViewById(R.id.selLicense)
        val approveButton: Button = findViewById(R.id.action_button)

        val paddingValue=20
        firstNameTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        emailTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        licenseTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        statusTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

        User.fetchUserByIdFromDatabase(userid) { res ->

            if (res != null) {
                var user = res as DrugSeller
                firstNameTextView.text = user.getFullName()
                emailTextView.text = user.getEmail()
                licenseTextView.text = user.getLicenceNumber()
                statusTextView.text = if (user.isApproved()) "Approved" else "Not Approved"

                user.getProfileImage(applicationContext, dImageView)
                user.getLicenseImage(applicationContext, licenseImageView)

                if (user.isApproved()) {
                    approveButton.text = ("DisApprove");
                    approveButton.setBackgroundColor(Color.RED)
                } else {
                    approveButton.text = ("Approve");
                    approveButton.setBackgroundColor(Color.BLUE)
                }

                approveButton.setOnClickListener(View.OnClickListener {

                    val message = if (user.isApproved()) {
                        "Are you sure you want to disapprove this Drug Seller ?"
                    } else {
                        "Are you sure you want to approve this Drug Seller?"
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