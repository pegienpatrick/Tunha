package com.tunha.ui.admins


import android.content.Intent
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.tunha.MainActivity
import com.tunha.R
import com.tunha.chemistDetails
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tunha.*


class Distributers : Fragment() {
    private var showPending=false

    companion object {
        fun newInstance() = Distributers()
    }

    private lateinit var viewModel: DistributersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



// Inflate the fragment layout
        val rootView = inflater.inflate(R.layout.fragment_distributers, container, false)



        val sach=rootView.findViewById<EditText>(R.id.sach)
        feedData(rootView,sach.text.toString())

        val dosearch=rootView.findViewById<ImageView>(R.id.dosearch)

        dosearch.setOnClickListener(View.OnClickListener {
            feedData(rootView,sach.text.toString())
        })

        val approved=rootView.findViewById<TextView>(R.id.approved)
        val pending=rootView.findViewById<TextView>(R.id.pending)




        approved.setOnClickListener(View.OnClickListener {
            approved.setTextColor(Color.BLUE)
            pending.setTextColor(Color.GRAY)
            showPending=false
            feedData(rootView,sach.text.toString())

        })

        pending.setOnClickListener(View.OnClickListener {
            pending.setTextColor(Color.BLUE)
            approved.setTextColor(Color.GRAY)
            showPending=true
            feedData(rootView,sach.text.toString())
        })


        approved.setTextColor(Color.BLUE)



        return rootView

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DistributersViewModel::class.java)
        // TODO: Use the ViewModel



    }

    fun feedData(view: View, sach: String){
        // Get a reference to the database node that contains the user information
        //val databaseRef = FirebaseDatabase.getInstance().getReference("users")

        // Get a reference to the parent LinearLayout that will hold the user items
        val parentLinearLayout = view.findViewById<LinearLayout>(R.id.holdData)

        val database = Firebase.database
        val myRef = database.getReference("users")

// Query data where the "email" field is equal to "test@example.com"
        var query = myRef.orderByChild("id").startAt("0").endAt("\uf8ff")
//            .equalTo("usertype","Admin")
//                if(sach.isNotEmpty()) {
//                    query = myRef.orderByChild("id").startAt("0").endAt("\uf8ff").orderByChild("usertype").equalTo("Admin")
//                        .orderByChild("email")
//                        .startAt(sach)
//                        .endAt(sach + "\uf8ff")
//                        .orderByChild("name")
//                        .startAt(sach)
//                        .endAt(sach + "\uf8ff")
//                }

        query.addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onDataChange: ")
                parentLinearLayout.removeAllViews()
                for (childSnapshot in dataSnapshot.children.reversed()) {
                    Log.d(ContentValues.TAG, "reading dta")
                    // Print the data for each child node
                    println("${childSnapshot.key} => ${childSnapshot.value}")
                    val us: User? = childSnapshot.getValue(User::class.java)


                    if (us != null) {

                        if(us.getUserType()=="DrugSeller") {
                            val dc=childSnapshot.getValue(DrugSeller::class.java)
                            if (dc != null) {
                                if(sach.isEmpty()||us.getEmail().contains(sach)||us.getFullName().contains(sach)||dc.getLicenceNumber().contains(sach)) {
                                    if((showPending!=dc.isApproved()))
                                        parentLinearLayout.addView(dc?.let { feedUser(it) })
//                                        else if(showPending!=dc.isApproved())
//                                            parentLinearLayout.addView(dc?.let { feedUser(it) })
                                }
                            }
                        }
                    }



                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    fun feedUser(user: User):LinearLayout
    {
        val parentLinearLayout = LinearLayout(context)
        parentLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        parentLinearLayout.orientation = LinearLayout.VERTICAL
        parentLinearLayout.setPadding(20, 20, 20, 20)

        val childLinearLayout = LinearLayout(context)
        childLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        childLinearLayout.orientation = LinearLayout.HORIZONTAL
        childLinearLayout.setPadding(16, 16, 16, 16)
        childLinearLayout.elevation = 4f
        //childLinearLayout.outlineProvider = BackgroundOutlineProvider()
        childLinearLayout.background = ColorDrawable(Color.parseColor("#FAFAFA"))

        val imageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(
            100,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.iconnny)
        imageView.foregroundGravity= Gravity.CENTER
        context?.let { user.getProfileImage(it, imageView) }
        imageView.setPadding(0, 0, 12, 0)
        imageView.clipToOutline = true
        imageView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.rounded_corner_background) }
        imageView.setPadding(20,18,20,18)


        val textLinearLayout = LinearLayout(context)
        textLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textLinearLayout.orientation = LinearLayout.VERTICAL
        textLinearLayout.setPadding(50, 0, 0, 0)

        val nameTextView = TextView(context)
        nameTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        nameTextView.text = user.getFullName()

        val emailTextView = TextView(context)
        emailTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        emailTextView.text = user.getEmail();

        textLinearLayout.addView(nameTextView)
        //textLinearLayout.addView(emailTextView)
        val tmplay=LinearLayout(context)
        tmplay.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tmplay.orientation=LinearLayout.HORIZONTAL

        var doc= TextView(context)
        var i=user as DrugSeller
        doc.text=i.getLicenceNumber()
        doc.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.0f
        )

        textLinearLayout.addView(tmplay)

        tmplay.addView(emailTextView)
        tmplay.addView(doc)




        childLinearLayout.addView(imageView)
        childLinearLayout.addView(textLinearLayout)

        parentLinearLayout.addView(childLinearLayout)

        return parentLinearLayout

    }

}