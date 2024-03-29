package com.tunha.ui.doctors


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        val lin=presdraftLayout.findViewById<LinearLayout>(R.id.all)

        val indiList: Array<LinearLayout> = arrayOf<LinearLayout>(
            presdraftLayout.findViewById(R.id.indi1),
            presdraftLayout.findViewById(R.id.indi2),
            presdraftLayout.findViewById(R.id.indi3),
            presdraftLayout.findViewById(R.id.indi4),
            presdraftLayout.findViewById(R.id.indi5),
            presdraftLayout.findViewById(R.id.indi6),
            presdraftLayout.findViewById(R.id.indi7)
        )

       lin.removeAllViews()
        var indicounter=-1


        val adder=view.findViewById<Button>(R.id.adddrug)

        val drugArray: Array<Array<View>> = Array(7) { i ->
            val packageName=requireContext().packageName
            arrayOf(
                indiList[i].findViewById(resources.getIdentifier("drugname${i+1}", "id", packageName)),
                indiList[i].findViewById(resources.getIdentifier("drugnamedosage${i+1}", "id", packageName)),
                indiList[i].findViewById(resources.getIdentifier("drugtimes${i+1}", "id", packageName)),
                indiList[i].findViewById(resources.getIdentifier("drugnametotalnumber${i+1}", "id", packageName))
            )
        }

        for(i in 0..6)
        {
            (drugArray[i][0] as Spinner).setSelection(-1)
        }




        adder.setOnClickListener(View.OnClickListener {
            if(indicounter<6)
            {
                indicounter++;
                parent.addView(indiList[indicounter])

                var s=Space(context)
                    s.layoutParams=LinearLayout.LayoutParams(20,30)
                parent.addView(s)
            }
            else
            {
                adder.visibility=View.INVISIBLE
            }

        })

        val snd=view.findViewById<Button>(R.id.buttonsubmit4)
            snd.setOnClickListener(View.OnClickListener {

                if(indicounter>-1)
                {
                    //val genderSpinner: Spinner = view.findViewById(R.id.gender_spinner)
                    val patientNameEditText: EditText = view.findViewById(R.id.patientname)
                    val genderSpinner: Spinner = view.findViewById(R.id.gender_spinner)
                    val ageEditText: EditText = view.findViewById(R.id.age)
                    var ids=""
                    ids= context?.let { it1 -> Session.getUserSession(it1).first.toString() }.toString()

                    var p:Prescription=Prescription(patientNameEditText.text.toString(),ids,genderSpinner.selectedItem.toString(),ageEditText.text.toString().toInt())

                    for(i in 0..indicounter)
                    {
                        if((drugArray[i][3] as EditText).text.toString().toInt()>0)
                        {
                            var d=DrugPrescription((drugArray[i][0] as Spinner).selectedItem.toString(),(drugArray[i][1] as EditText).text.toString().toInt(),(drugArray[i][2] as EditText).text.toString().toInt(),(drugArray[i][3] as EditText).text.toString().toInt()  )
                            p.addDrug(d)

                        }
                    }

                    //p.addToFirebase()
                    var rec:Int=-1
                    p.generateUniqueId {id->
                       if (id != null) {
                           rec=id
                           // remove all views from the linear layout
                           // get a reference to the parent LinearLayout
                           // get a reference to the parent LinearLayout
                           // get a reference to the parent LinearLayout
                           // get a reference to the parent LinearLayout
                           // get a reference to the parent LinearLayout
                           val parentLayout = view.findViewById<LinearLayout>(R.id.whole)

// remove all the child views from the parent LinearLayout
                           parentLayout.removeAllViews()

// create a new TextView for the message and set its text, background color, text color, and padding
                           val messageTextView = TextView(context)
                           messageTextView.text = "Prescription Successful"
                           messageTextView.setBackgroundColor(Color.rgb(151, 203, 153)) // light green
                           messageTextView.setTextColor(Color.WHITE) // white text
                           messageTextView.setPadding(32, 32, 32, 32)
                           messageTextView.textSize = 32f // set font size to 32sp
                           messageTextView.setTypeface(null, Typeface.BOLD) // set bold font

// create a new LinearLayout to hold the prescription number TextViews
                           val prescriptionNumberLayout = LinearLayout(context)
                           prescriptionNumberLayout.orientation = LinearLayout.VERTICAL // set orientation to vertical

// create a new TextView for the prescription number label and set its text, text color, and font size
                           val prescriptionNumberLabelTextView = TextView(context)
                           prescriptionNumberLabelTextView.text = "Prescription number:"
                           prescriptionNumberLabelTextView.setTextColor(Color.rgb(39, 71, 123)) // dark blue text
                           prescriptionNumberLabelTextView.textSize = 24f // set font size to 24sp

// create a new TextView for the prescription number value and set its text, text color, and font size
                           val prescriptionNumberValueTextView = TextView(context)
                           prescriptionNumberValueTextView.text = rec.toString()
                           prescriptionNumberValueTextView.setTextColor(Color.rgb(39, 71, 123)) // dark blue text
                           prescriptionNumberValueTextView.textSize = 48f // set font size to 48sp
                           prescriptionNumberValueTextView.setTypeface(null, Typeface.BOLD) // set bold font

// add the prescription number label and value TextViews as child views of the prescription number LinearLayout
                           prescriptionNumberLayout.addView(prescriptionNumberLabelTextView)
                           prescriptionNumberLayout.addView(prescriptionNumberValueTextView)

// add the message and prescription number LinearLayouts as child views of the parent LinearLayout
                           parentLayout.addView(messageTextView)
                           parentLayout.addView(prescriptionNumberLayout)










                           val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                           builder?.setTitle("Prescription Successful")
                           builder?.setMessage("The Prescription number is $rec")
                           builder?.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                           val dialog = builder?.create()

                           dialog?.show()




                       };
                    }

                }

            })








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

fun cloneLinearLayout(linearLayout: LinearLayout): LinearLayout {
    val newLinearLayout = LinearLayout(linearLayout.context)
    newLinearLayout.layoutParams = linearLayout.layoutParams
    newLinearLayout.orientation = linearLayout.orientation
    newLinearLayout.gravity = linearLayout.gravity
    newLinearLayout.background = linearLayout.background
    newLinearLayout.setPadding(
        linearLayout.paddingLeft, linearLayout.paddingTop,
        linearLayout.paddingRight, linearLayout.paddingBottom
    )

    for (i in 0 until linearLayout.childCount) {
        val childView = linearLayout.getChildAt(i)
        val newChildView = cloneView(childView)
        newLinearLayout.addView(newChildView)
    }

    return newLinearLayout
}

@Suppress("UNCHECKED_CAST")
fun <T : View> cloneView(view: T): T {
    val clonedView = view.javaClass.constructors[0].newInstance(view.context) as T
    clonedView.layoutParams = view.layoutParams
    clonedView.id = view.id

    if (view is TextView && clonedView is TextView) {
        clonedView.text = view.text
        clonedView.gravity = view.gravity
        clonedView.textSize = view.textSize
        clonedView.setTextColor(view.currentTextColor)
        clonedView.setTypeface(view.typeface)
    }

    if (view is ImageView && clonedView is ImageView) {
        clonedView.setImageDrawable(view.drawable)
    }

    return clonedView
}



