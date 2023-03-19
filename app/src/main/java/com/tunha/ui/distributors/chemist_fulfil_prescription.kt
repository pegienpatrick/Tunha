package com.tunha.ui.distributors

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tunha.Prescription
import com.tunha.R
import com.tunha.Session
import java.text.SimpleDateFormat
import java.util.*

class chemist_fulfil_prescription : Fragment() {

    companion object {
        fun newInstance() = chemist_fulfil_prescription()
    }

    private lateinit var viewModel: ChemistFulfilPrescriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


       var rootView=inflater.inflate(R.layout.fragment_chemist_fulfil_prescription, container, false)



        val sach=rootView.findViewById<EditText>(R.id.sach)


        val dosearch=rootView.findViewById<ImageView>(R.id.dosearch)

        dosearch.setOnClickListener(View.OnClickListener {
            feedPrescriptions(rootView,sach.text.toString())
        })
        feedPrescriptions(rootView, sach.text.toString())

        return rootView
    }

    private fun feedPrescriptions(rootView: View, sach: String) {


        val parentLinearLayout = rootView.findViewById<LinearLayout>(R.id.holdData2)
        //parentLinearLayout.setBackgroundColor(Color.BLUE)

        val database = Firebase.database
        val myRef = database.getReference("prescriptions")


        var ids: String? = context?.let { Session.getUserSession(it).first.toString() }
        var query = myRef.orderByChild("UniqueId")


        query.addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ")
                parentLinearLayout.removeAllViews()
                if(sach.isEmpty())
                {
                    parentLinearLayout.addView(TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1f)
                    text = "Search Something"
                        gravity=Gravity.CENTER
                })
                }
                var found=false;

                for (childSnapshot in dataSnapshot.children.reversed()) {
                    Log.d(TAG, "reading dta")
                    // Print the data for each child node
                    println("${childSnapshot.key} => ${childSnapshot.value}")

                    var p: Prescription? =childSnapshot.getValue(Prescription::class.java)
                    //parentLinearLayout.addView(p?.let { feedPrescription(it) })
                    if(p!=null)
                    {
                        if(sach.isEmpty()) {

//                             parentLinearLayout.addView(feedPrescription(p))
//                             parentLinearLayout.addView(Space(context).apply {
//                                 layoutParams = LinearLayout.LayoutParams(100, 30)
//                             })


                        }
                        else if(p.getUniqueId().toString().contains(sach)||p.name.contains(sach)){
                            parentLinearLayout.addView(feedPrescription(p))
                            parentLinearLayout.addView(Space(context).apply {
                                layoutParams = LinearLayout.LayoutParams(100, 30)
                            })
                            found=true
                        }
                    }


                }
                if(!found)
                {
                    parentLinearLayout.addView(TextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                        text = "No Prescription Found"
                        gravity=Gravity.CENTER
                    })

                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        //parentLinearLayout.addView(feedPrescription(Prescription()) )
        //parentLinearLayout.addView(feedPrescription(Prescription()) )

    }

    @SuppressLint("SetTextI18n")
    fun feedPrescription(prescription: Prescription):LinearLayout
    {
        // Create the outer LinearLayout
        val outerLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
            setPadding(20, 20, 20, 20)
        }

        // Create the first LinearLayout with 3 TextViews
        val firstLinearLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL
        }

        val firstTextView1 = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = "# "+prescription.getUniqueId()
        }

        val firstTextView2 = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = prescription.name
        }

        val firstTextView3 = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = if(prescription.getFulfilledBy().isNotEmpty())"Fulfilled" else "Pending"
        }

        firstLinearLayout.addView(firstTextView1)
        firstLinearLayout.addView(firstTextView2)
        firstLinearLayout.addView(firstTextView3)

        // Create the second LinearLayout with 3 TextViews
        val secondLinearLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL
        }

        val secondTextView1 = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = prescription.gender
        }

        val secondTextView2 = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = prescription.age.toString()+" yrs"
        }

        val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val secondTextView3 = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text =sdf.format( prescription.getTime())
        }

        secondLinearLayout.addView(secondTextView1)
        secondLinearLayout.addView(secondTextView2)
        secondLinearLayout.addView(secondTextView3)



        outerLayout.addView(firstLinearLayout)
        outerLayout.addView(secondLinearLayout)

        outerLayout.addView(Space(context).apply {
            layoutParams = LinearLayout.LayoutParams(100, 20)
        })
        for(drug in prescription.getDrugs())
        {

            // Create the third LinearLayout with 3 TextViews and a Space view
            val thirdLinearLayout = LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                orientation = LinearLayout.HORIZONTAL
            }

            val spaceView = Space(context).apply {
                layoutParams = LinearLayout.LayoutParams(100, WRAP_CONTENT)
            }

            val thirdTextView1 = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                text = drug.getName()
            }

            val thirdTextView2 = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                text = drug.getDosage().toString()+" X "+drug.getTimes().toString()
            }

            val thirdTextView3 = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                text = drug.getTotal().toString()
            }

            thirdLinearLayout.addView(Space(context).apply {
                layoutParams = LinearLayout.LayoutParams(100, WRAP_CONTENT)
            })
            thirdLinearLayout.addView(thirdTextView1)
            thirdLinearLayout.addView(thirdTextView2)
            thirdLinearLayout.addView(thirdTextView3)



            outerLayout.addView(thirdLinearLayout)
        }

        outerLayout.setOnClickListener(View.OnClickListener {

            if(prescription.getFulfilledBy().isEmpty()) {
                var intent = Intent(activity, FulfilPrescription::class.java)
                intent.putExtra("prescription", prescription.getUniqueId().toString())
                startActivity(intent)
            }
            else
                Toast.makeText(context,"Already Fulfilled",Toast.LENGTH_SHORT)

        })


        return  outerLayout
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChemistFulfilPrescriptionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}