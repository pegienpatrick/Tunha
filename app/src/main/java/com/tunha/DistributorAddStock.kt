package com.tunha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize


class DistributorAddStock : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_add_stock)

        Firebase.initialize(applicationContext)

        val ids=Session.getUserSession(applicationContext).first.toString()
        val drug=findViewById<Spinner>(R.id.chooseDrug)
        val qua=findViewById<EditText>(R.id.number2)

        var snd=findViewById<Button>(R.id.sell)
        snd.setOnClickListener(View.OnClickListener {

            Stock.fetchStock(ids+"_"+drug.selectedItem.toString()
            ) { stock:Stock? ->
                if (stock != null) {
                    stock.setAvailableNo(stock.getAvailableNo() + qua.text.toString().toInt())
                    stock.addToDatabase()
                } else {
                    Stock(
                        ids + "_" + drug.selectedItem.toString(),
                        qua.text.toString().toInt(),
                        0
                    ).addToDatabase()

                }

            }

            finish()



        })
    }
}