package com.tunha

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class Prescription(
    val name: String,
    val doctor: String,
    val gender: String,
    val age: Int
) {
    private var drugs: MutableList<DrugPrescription> = mutableListOf()
    private var uniqueId: Int? = null
    private var fulfilledBy=""
    private var whendone= Date()

    constructor(): this("", "", "", 0)

//    init {
//        if (uniqueId == null) {
//            uniqueId = generateUniqueId()
//        }
//    }

    public fun getTime():Date
    {
        return whendone;
    }

//    public fun getName():String{
//        return name
//    }

    public fun generateUniqueId(task: (Int?) -> Unit): Int {
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
                addToFirebase()
                task(id)
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

    fun getUniqueId(): Int? {
        return uniqueId
    }

    fun getFulfilledBy(): String {
        return fulfilledBy
    }

    fun setUniqueId(id: Int) {
        uniqueId = id
    }

    fun setFulfilledBy(fulfilledBy: String) {
        this.fulfilledBy = fulfilledBy
    }


    companion object {
        @JvmStatic // Optional annotation to make the function callable from Java code
        fun fetchPrescriptionsFromDatabase(task: (List<Prescription>) -> Unit) {
            val database = Firebase.database
            val databaseRef: DatabaseReference = database.getReference("prescriptions")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val PrescriptionList = mutableListOf<Prescription>()
                    for (PrescriptionSnapshot in dataSnapshot.children) {
                        var us: Prescription? = PrescriptionSnapshot.getValue(Prescription::class.java)
                        if (us != null) {
                            Log.d(TAG,"list gotten"+PrescriptionSnapshot)
                            PrescriptionList.add(us)
                        }
                    }
                    task(PrescriptionList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }


        fun fetchPrescriptionByIdFromDatabase(PrescriptionId: String, task: (Prescription?) -> Unit) {
            val database = Firebase.database
            val databaseRef: DatabaseReference = database.getReference("prescriptions").child(PrescriptionId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val prescription: Prescription? = dataSnapshot.getValue(Prescription::class.java)
                    if (prescription != null) {
                       
                        task(prescription)
                    } else {
                        task(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    task(null)
                }
            })
        }


    }




}





