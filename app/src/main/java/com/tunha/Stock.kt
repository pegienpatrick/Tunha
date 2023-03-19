package com.tunha

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Stock {

    private var _ownerDrug: String
    private var _availableNo: Int
    private var _expiredNo: Int




    // Parameterized constructor
    constructor(ownerDrug: String, availableNo: Int, expiredNo: Int)  {
        _ownerDrug = ownerDrug
        _availableNo = availableNo
        _expiredNo = expiredNo
    }


    constructor():this("",0,0)
    {

    }

    // Getters
    fun getOwnerDrug(): String {
        return _ownerDrug
    }

    fun getAvailableNo(): Int {
        return _availableNo
    }

    fun getExpiredNo(): Int {
        return _expiredNo
    }

    // Setters
    fun setOwnerDrug(ownerDrug: String) {
        _ownerDrug = ownerDrug
    }

    fun setAvailableNo(availableNo: Int) {
        _availableNo = availableNo
    }

    fun setExpiredNo(expiredNo: Int) {
        _expiredNo = expiredNo
    }

    fun addToDatabase(){
        val database = Firebase.database.getReference("stock")
        database.child(_ownerDrug).setValue(this)
    }

    companion object {
      
        fun fetchStock(ownerDrug: String, task: (Stock?) -> Unit) {
            val database = Firebase.database
            val databaseRef: DatabaseReference = database.getReference("stock").child(ownerDrug)

            // Get the name of the calling method
            val methodName = Thread.currentThread().stackTrace[3].methodName

            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //val user: User? = dataSnapshot.getValue(User::class.java)
                    val st=dataSnapshot.getValue(Stock::class.java)
                    task(st)
                    
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    task(null)
                }
            })
        }





    }
}



