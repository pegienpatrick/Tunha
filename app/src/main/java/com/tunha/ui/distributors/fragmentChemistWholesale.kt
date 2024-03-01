package com.tunha.ui.distributors

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tunha.*

class fragmentChemistWholesale : Fragment() {

    private var showPending=false

    companion object {
        fun newInstance() = fragmentChemistWholesale()
    }

    private lateinit var viewModel: FragmentChemistWholesaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_fragment_chemist_wholesale, container, false)



        val sach=rootView.findViewById<EditText>(R.id.sach)
        feedData(rootView,sach.text.toString())

        val dosearch=rootView.findViewById<ImageView>(R.id.dosearch)

        dosearch.setOnClickListener(View.OnClickListener {
            feedData(rootView,sach.text.toString())
        })




        return rootView

    }



    fun feedData(view: View, sach: String){
        // Get a reference to the database node that contains the user information
        //val databaseRef = FirebaseDatabase.getInstance().getReference("users")

        // Get a reference to the parent LinearLayout that will hold the user items
        val parentLinearLayout = view.findViewById<LinearLayout>(R.id.holdData)

        val database = Firebase.database
        val myRef = database.getReference("users")

        var ids= context?.let { Session.getUserSession(it).first.toString() }

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

                        if(us.getUserType()=="DrugSeller"&&us.getId()!=ids) {
                            val dc=childSnapshot.getValue(DrugSeller::class.java)
                            if (dc != null&&sach.isNotEmpty()) {
                                if(us.getEmail().contains(sach)||us.getFullName().contains(sach)||dc.getLicenceNumber().contains(sach)) {
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

        parentLinearLayout.setOnClickListener(View.OnClickListener {
            Log.d(TAG,"opening")
            var intent:Intent=android.content.Intent(context,DrugSellWholesale::class.java)
            intent.putExtra("receiver",user.getId())
            startActivity(intent)
        })

        return parentLinearLayout

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentChemistWholesaleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}