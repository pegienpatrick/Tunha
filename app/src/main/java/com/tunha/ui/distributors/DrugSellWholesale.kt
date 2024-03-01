package com.tunha.ui.distributors

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tunha.*

class DrugSellWholesale : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_sell_wholesale)

        val backButton = findViewById<ImageView>(R.id.backbutton)
        backButton.setOnClickListener {
            finish()
        }
        var i=intent
        if (i.hasExtra("receiver")) {
            var userid = i.getStringExtra("receiver")

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
     

        val paddingValue=20
        firstNameTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        emailTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        statusTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

        User.fetchUserByIdFromDatabase(userid) { res ->

            if (res != null) {
                var user = res as DrugSeller
                firstNameTextView.text = user.getFullName()
                emailTextView.text = user.getEmail()
                licenseTextView.text = user.getLicenceNumber()
                statusTextView.text = if (user.isApproved()) "Approved" else "Not Approved"

                user.getProfileImage(applicationContext, dImageView)
                

                

            }


        }


        feedForm(userid)


    }

    fun feedForm(userid:String)
    {
    	val ids= userid
        val table = findViewById<TableLayout>(R.id.table)
        val drugNames = resources.getStringArray(R.array.drugs)

        

        val database = Firebase.database.getReference("stock")



        val fields: MutableList<EditText> = mutableListOf()

        for (drugName in drugNames) {
            val row = TableRow(applicationContext)
            val layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                WRAP_CONTENT
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


                        val ff = EditText(applicationContext)
                        ff.inputType = InputType.TYPE_CLASS_NUMBER

                        ff.layoutParams=lp

                        row.addView(ff)
                        fields.add(ff)




                    } else {
                        // If stock data exists, retrieve and display the values
                        val availableView = TextView(applicationContext)
                        availableView.text = stock.getAvailableNo().toString()
                        row.addView(availableView)
                        availableView.layoutParams=lp
                        availableView.gravity=Gravity.CENTER


                        val ff = EditText(applicationContext)
                        ff.inputType = InputType.TYPE_CLASS_NUMBER

                        ff.layoutParams=lp

                        row.addView(ff)
                        fields.add(ff)

                        if(stock.getAvailableNo()>=50)
                        {
                            ff.setBackgroundColor(Color.RED)
                            ff.isEnabled=false
                        }


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Failed to retrieve data", error.toException())
                }
            })

            table.addView(row)
        }


        val sell=findViewById<Button>(R.id.action_button)
        sell.setOnClickListener(View.OnClickListener {
            var from=Session.getUserSession(applicationContext).first.toString()
            var to=userid
            for(i in drugNames.indices)
            {
                var tty=0
                try {
                    tty = fields[i].text.toString().toInt()
                    // Do something with the integer...
                } catch (e: NumberFormatException) {
                    // Handle the case where the input is not a valid integer
                }
                val tt=tty
                if(tt>0)
                {

                    Stock.fetchStock(from+"_"+drugNames[i]){
                        stock->
                        if(stock!=null)
                            if(tt<stock.getAvailableNo())
                            {
                                stock.setAvailableNo(stock.getAvailableNo()-tt)
                                stock.addToDatabase()
                                Stock.fetchStock(to+"_"+drugNames[i]){
                                    st->
                                    if(st!=null) {
                                        st.setAvailableNo(st.getAvailableNo() + tt)
                                        st.addToDatabase()
                                    }
                                    else
                                    {
                                        Stock(to+"_"+drugNames[i],tt,0).addToDatabase()

                                    }

                                }
                                Toast.makeText(applicationContext,"Sale SuccessFul",Toast.LENGTH_SHORT)
                            }


                    }
                }

            }
            finish()

        })




    }




}