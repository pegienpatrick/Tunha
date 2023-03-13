package com.tunha

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class Prescription(
    val name: String,
    val doctor: String,
    val uniqueId: String,
    val gender: String,
    val age: Int,
    private var drugs: List<DrugPrescription>
) {

    fun addToFirebase() {
        val database = Firebase.database
        val myRef: DatabaseReference = database.getReference("prescriptions")

        myRef.child(this.uniqueId.toString()).setValue(this)
    }

    fun addDrugs(i:DrugPrescription){
        this.drugs.add(i)
    }

    companion object {
        @JvmStatic // Optional annotation to make the function callable from Java code
        fun fetchPrescriptionsFromDatabase(task: (List<Prescription>) -> Unit) {
            val database = Firebase.database
            val databaseRef: DatabaseReference = database.getReference("prescriptions")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val prescriptionList = mutableListOf<Prescription>()
                    for (prescriptionSnapshot in dataSnapshot.children) {
                        var prescription: Prescription? = prescriptionSnapshot.getValue(Prescription::class.java)
                        if (prescription != null) {
                            prescriptionList.add(prescription)
                        }
                    }
                    task(prescriptionList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
}


data class DrugPrescription(
    val name: String,
    val dosage: String,
    val times: String,
    val total: Int
)
