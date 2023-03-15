package com.tunha

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Prescription(
    val name: String,
    val doctor: String,
    var uniqueId: Int? = null,
    val gender: String,
    val age: Int,
    private val drugs: MutableList<DrugPrescription> = mutableListOf()
) {
    constructor(): this("", "", null, "", 0)

    init {
        if (uniqueId == null) {
            uniqueId = generateUniqueId()
        }
    }

    private fun generateUniqueId(): Int {
        val database = Firebase.database
        val idRef = database.getReference("uniqueIdCounter")

        idRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var id = dataSnapshot.getValue(Int::class.java)
                if (id == null) {
                    id = 0
                }
                id++
                uniqueId=id;
                idRef.setValue(id)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        return 0 // Return a default value, since the actual ID value will be determined asynchronously
    }






    fun addToFirebase() {
        val database = Firebase.database
        val myRef: DatabaseReference = database.getReference("prescriptions")

        myRef.child(uniqueId.toString()).setValue(this)
    }

    fun addDrug(drug: DrugPrescription) {
        drugs.add(drug)
    }

    fun getDrugs(): MutableList<DrugPrescription> {
        return drugs
    }
}





