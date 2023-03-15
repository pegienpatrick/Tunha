package com.tunha.ui.doctors


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tunha.*


class doctorAddPrescription : Fragment() {

    companion object {
        fun newInstance() = doctorAddPrescription()
    }

    private lateinit var viewModel: DoctorAddPrescriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        return inflater.inflate(R.layout.fragment_doctor_add_prescription, container, false)
        val view = inflater.inflate(R.layout.fragment_doctor_add_prescription, container, false)

        val genderSpinner: Spinner = view.findViewById(R.id.gender_spinner)
        val genders = arrayOf("Male", "Female")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        genderSpinner.prompt = "Select gender"


        feedDrugs(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoctorAddPrescriptionViewModel::class.java)
        // TODO: Use the ViewModel

    }


    @SuppressLint("ResourceType", "SuspiciousIndentation")
    fun feedDrugs(view:View){
        val parent=view.findViewById<LinearLayout>(R.id.holdData)
        val inflater = LayoutInflater.from(context)
        val presdraftLayout = inflater.inflate(R.layout.presdraft, parent, false)
        parent.addView(presdraftLayout)

        //parent.addView(R.layout.presdraft)
    //     val fieldsDrugname=listOf<TextView>()
    //     val fieldsdosage=listOf<EditText>()
    //     val fieldstimes=listOf<EditText>()
    //     val fieldstotal=listOf<EditText>()
    //     if(context!=null)
    //         for (drug in Drugs.antibiotics) {


    //         val layoutParams = TableRow.LayoutParams(
    //             TableRow.LayoutParams.MATCH_PARENT,
    //             TableRow.LayoutParams.WRAP_CONTENT
    //         )
    //         layoutParams.setMargins(20, 20, 20, 20)

    //         val linearLayout = LinearLayout(requireContext())
    //         linearLayout.layoutParams = LinearLayout.LayoutParams(
    //             LinearLayout.LayoutParams.MATCH_PARENT,
    //             LinearLayout.LayoutParams.WRAP_CONTENT
    //         )
    //         linearLayout.orientation = LinearLayout.VERTICAL
    //         linearLayout.setPadding(20, 20, 20, 20)
    //         linearLayout.setBackgroundColor(Color.LTGRAY)

    //         // Create a new table layout for each drug
    //         val tableLayout = TableLayout(context)
    //         tableLayout.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
    //         tableLayout.gravity = Gravity.CENTER


    //         // Add drug name row
    //         val drugNameRow = TableRow(context)
    //         drugNameRow.gravity = Gravity.CENTER
    //         drugNameRow.layoutParams=layoutParams
    //         //drugNameRow.setBackgroundColor(Color.RED)

    //         val drugNameTitle = TextView(context)
    //         drugNameTitle.text = buildString {
    //     append("Drug Name")
    // }
    //         drugNameRow.addView(drugNameTitle)

    //         val colon = TextView(context)
    //         colon.text = " : "
    //         drugNameRow.addView(colon)

    //         val drugName = TextView(context)
    //         drugName.text = drug
    //         drugNameRow.addView(drugName)

    //             val displayMetrics = resources.displayMetrics
    //             val screenWidth = displayMetrics.widthPixels
    //         drugName.layoutParams.width=screenWidth/3+50
    //             drugNameTitle.layoutParams.width=screenWidth/3+50

    //         tableLayout.addView(drugNameRow)

    //         // Add dosage and times a day rows
    //             val dosageRow = TableRow(context)
    //             dosageRow.gravity = Gravity.CENTER

    //             if(context!=null) {
    //                 val dosageLayout = TextInputLayout(requireContext())
    //                 dosageLayout.layoutParams = TableRow.LayoutParams(
    //                     TableRow.LayoutParams.MATCH_PARENT,
    //                     TableRow.LayoutParams.WRAP_CONTENT
    //                 )
    //                 dosageLayout.hint = "Dosage"
    //                 dosageLayout.setPadding(2,5,2,5)

    //                 val dosageEditText = TextInputEditText(requireContext())
    //                 dosageEditText.id =
    //                     View.generateViewId() // Generate a unique ID for each EditText
    //                 dosageEditText.layoutParams = LinearLayout.LayoutParams(
    //                     LinearLayout.LayoutParams.MATCH_PARENT,
    //                     LinearLayout.LayoutParams.WRAP_CONTENT
    //                 )
    //                 dosageEditText.inputType = InputType.TYPE_CLASS_NUMBER

    //                 dosageLayout.addView(dosageEditText)
    //                 dosageRow.addView(dosageLayout)

    //                 val timesADay = TextView(context)
    //                 timesADay.text = " X "
    //                 dosageRow.addView(timesADay)

    //                 val timesLayout = context?.let { TextInputLayout(it) }
    //                 if (timesLayout != null) {
    //                     timesLayout.layoutParams = TableRow.LayoutParams(
    //                         TableRow.LayoutParams.MATCH_PARENT,
    //                         TableRow.LayoutParams.WRAP_CONTENT
    //                     )
    //                 }
    //                 if (timesLayout != null) {
    //                     timesLayout.hint = "Times a day"
    //                 }

    //                 val timesEditText = TextInputEditText(requireContext())
    //                 timesEditText.id =
    //                     View.generateViewId() // Generate a unique ID for each EditText
    //                 timesEditText.layoutParams = LinearLayout.LayoutParams(
    //                     LinearLayout.LayoutParams.MATCH_PARENT,
    //                     LinearLayout.LayoutParams.WRAP_CONTENT
    //                 )
    //                 timesEditText.inputType = InputType.TYPE_CLASS_NUMBER

    //                 timesLayout?.addView(timesEditText)
    //                 dosageRow.addView(timesLayout)

    //                 tableLayout.addView(dosageRow)

    //                 // Add total number row
    //                 val totalRow = TableRow(context)
    //                 totalRow.gravity = Gravity.CENTER

    //                 val totalTitle = TextView(context)
    //                 totalTitle.text = "Total "
    //                 totalRow.addView(totalTitle)

    //                 val times = TextView(context)
    //                 times.text = " : "
    //                 totalRow.addView(times)

    //                 val totalNumberLayout = TextInputLayout(requireContext())
    //                 totalNumberLayout.layoutParams = TableRow.LayoutParams(
    //                     TableRow.LayoutParams.MATCH_PARENT,
    //                     TableRow.LayoutParams.WRAP_CONTENT
    //                 )
    //                 totalNumberLayout.hint = "Total number"

    //                 val totalNumberEditText = context?.let { TextInputEditText(it) }
    //                 if (totalNumberEditText != null) {
    //                     totalNumberEditText.id =
    //                         View.generateViewId()
    //                 } // Generate a unique ID for each EditText
    //                 if (totalNumberEditText != null) {
    //                     totalNumberEditText.layoutParams = LinearLayout.LayoutParams(
    //                         LinearLayout.LayoutParams.MATCH_PARENT,
    //                         LinearLayout.LayoutParams.WRAP_CONTENT
    //                     )
    //                 }
    //                 if (totalNumberEditText != null) {
    //                     totalNumberEditText.inputType = InputType.TYPE_CLASS_NUMBER
    //                 }

    //                 totalNumberLayout.addView(totalNumberEditText)
    //                 totalRow.addView(totalNumberLayout)

    //                 tableLayout.addView(totalRow)

    //                 // Add space at the end
    //                 val space = Space(context)
    //                 space.layoutParams = LinearLayout.LayoutParams(
    //                     LinearLayout.LayoutParams.WRAP_CONTENT,
    //                     70
    //                 )

    //                 fieldsDrugname.add(drugNameTextView)
    //                 fieldsdosage.add(dosageEditText)
    //                 fieldstimes.add(timesEditText)
    //                 fieldstotal.add(totalNumberEditText

    //                 // Add the table layout to the parent view
    //                 linearLayout.addView(tableLayout)

    //                 parent.addView(linearLayout)
    //                 parent.addView(space)
    //             }
    //     }

    //     val snd=view.findViewById<Button>(R.id.buttonSubmit4)
    //     snd.setOnClickListener(View.OnClickListener {

    //         val patientNameEditText: EditText = view.findViewById(R.id.patientname)
    //         val genderSpinner: Spinner = view.findViewById(R.id.gender_spinner)
    //         val ageEditText: EditText = view.findViewById(R.id.age)

    //         val prescribeButton: Button = view.findViewById(R.id.buttonsubmit4)

    //         val database = Firebase.database
    //         val myRef: DatabaseReference = database.getReference("prescriptions")
    //         val prescriptionId = myRef.push().key.substring(0,4)

    //         val ids=context?.let { it1 -> Session.getUserSession(it1).first.toString() }



    //                 if (prescriptionId != null) {
    //                     var p: Prescription? =
    //                         ids?.let { it1 ->
    //                             Prescription(
    //                                 patientNameEditText.text.toString(),
    //                                 it1,
    //                                 prescriptionId,
    //                                 genderSpinner.selectedItem.toString(),
    //                                 ageEditText.text.toString().toInt(),
    //                                 listOf()
    //                             )
    //                         }
    //                     for (i in fieldstotal.indices) {
    //                         if (fieldstotal[i].text.toString().toInt()>0){
    //                             if (p != null) {
    //                                 p.addDrugs(DrugPrescription(fieldsDrugname[i].text.toString(),fieldsdosage[i].text.toString(),fieldstimes[i].text.toString(),fieldstotal.text.toString()))
    //                             };
    //                         }
    //                 }
    //                     if (p != null) {
    //                         p.addToFirebase()
    //                     }



    //     }

    //     })

    }






}