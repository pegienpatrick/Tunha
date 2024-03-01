package com.tunha.ui.distributors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tunha.DistributorAddStock
import com.tunha.R
import com.tunha.Session
import com.tunha.Stock
import java.util.*


class distributorStockManagement : Fragment() {

    companion object {
        fun newInstance() = distributorStockManagement()
    }

    private lateinit var viewModel: DistributorStockManagementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view = inflater.inflate(R.layout.fragment_distributor_stock_management, container,false)
        val button = view.findViewById<Button>(R.id.addStock)

        button.setOnClickListener{
            val intent = Intent(activity, DistributorAddStock::class.java)

            activity?.startActivity(intent)
        }

        val b2=view.findViewById<Button>(R.id.reportExpired)
        b2.setOnClickListener(View.OnClickListener {
            val intent=Intent(activity,ReportExpired::class.java)

            startActivity(intent)
        })

        feedData(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DistributorStockManagementViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun feedData(view:View)
    {
        val ids= context?.let { Session.getUserSession(it).first.toString() }
        val table = view.findViewById<TableLayout>(R.id.table)
        val drugNames = resources.getStringArray(R.array.drugs)

        val database = Firebase.database.getReference("stock")

        for (drugName in drugNames) {
            val row = TableRow(context)
            val layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                WRAP_CONTENT
            )
            layoutParams.weight = 1.0f
            //row.layoutParams = layoutParams

            val drugNameView = TextView(context)
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
                        val availableView = TextView(context)
                        availableView.text = newStock.getAvailableNo().toString()

                        row.addView(availableView)
                        availableView.layoutParams=lp
                        availableView.gravity=Gravity.CENTER

                        val expiredView = TextView(context)
                        expiredView.text = newStock.getExpiredNo().toString()
                        row.addView(expiredView)
                        expiredView.layoutParams=lp
                        expiredView.gravity=Gravity.CENTER

                    } else {
                        // If stock data exists, retrieve and display the values
                        val availableView = TextView(context)
                        availableView.text = stock.getAvailableNo().toString()
                        row.addView(availableView)
                        availableView.layoutParams=lp
                        availableView.gravity=Gravity.CENTER

                        val expiredView = TextView(context)
                        expiredView.text = stock.getExpiredNo().toString()
                        row.addView(expiredView)
                        expiredView.layoutParams=lp
                        expiredView.gravity=Gravity.CENTER
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