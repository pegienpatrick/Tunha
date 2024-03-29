package com.tunha

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
                feedStock(userid)

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


    fun feedStock(ids:String)
    {
        val table = findViewById<TableLayout>(R.id.table)
        val drugNames = resources.getStringArray(R.array.drugs)

        val database = Firebase.database.getReference("stock")

        for (drugName in drugNames) {
            val row = TableRow(applicationContext)
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.weight = 1.0f
            //row.layoutParams = layoutParams

            val drugNameView = TextView(applicationContext)
            drugNameView.text = drugName
            row.addView(drugNameView)

            // Retrieve or create stock data for this drug
            val query = database.child(ids+"_$drugName")//orderByChild("ownerDrug").equalTo(ids+"_$drugName")

            val lp = TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            drugNameView.layoutParams=lp
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val stock = snapshot.getValue(Stock::class.java)

                    if (stock == null) {
                        // If no stock data exists, create a new entry
                        val newStock = Stock(ids+"_$drugName", 0, 0)
                        //database.push().setValue(newStock)
                        newStock.addToDatabase()
                        // Add the new stock data to the table with default values
                        val availableView = TextView(applicationContext)
                        availableView.text = newStock.getAvailableNo().toString()

                        row.addView(availableView)
                        availableView.layoutParams=lp
                        availableView.gravity= Gravity.CENTER

                        val expiredView = TextView(applicationContext)
                        expiredView.text = newStock.getExpiredNo().toString()
                        row.addView(expiredView)
                        expiredView.layoutParams=lp
                        expiredView.gravity= Gravity.CENTER

                    } else {
                        // If stock data exists, retrieve and display the values
                        val availableView = TextView(applicationContext)
                        availableView.text = stock.getAvailableNo().toString()
                        row.addView(availableView)
                        availableView.layoutParams=lp
                        availableView.gravity= Gravity.CENTER

                        val expiredView = TextView(applicationContext)
                        expiredView.text = stock.getExpiredNo().toString()
                        row.addView(expiredView)
                        expiredView.layoutParams=lp
                        expiredView.gravity= Gravity.CENTER
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Failed to retrieve data", error.toException())
                }
            })

            table.addView(row)
        }
    }
}