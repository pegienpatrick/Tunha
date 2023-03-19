package com.tunha.ui.distributors

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.tunha.*
import java.text.SimpleDateFormat
import java.util.*

class FulfilPrescription : AppCompatActivity() {
    private var prescription:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fulfil_prescription)

        prescription= intent.extras?.getString("prescription")

        val backButton = findViewById<ImageView>(R.id.backbutton)
        backButton.setOnClickListener {
            finish()
        }


        feedData()

    }


    fun feedData(){

        val ids=Session.getUserSession(applicationContext).first.toString()

        val hld=findViewById<LinearLayout>(R.id.holdData)

        val btn=findViewById<Button>(R.id.action_button)

        prescription?.let {
            Prescription.fetchPrescriptionByIdFromDatabase(it){
                ps->
                if(ps!=null) {
                    var dkt = ps.doctor

                    User.fetchUserByIdFromDatabase(dkt){
                        user->
                            if(user!=null) {
                                hld.addView(feedUser(user))
                                hld.addView(feedPrescription(ps))
                            }
                    }


                    for(i in ps.getDrugs())
                    {
                        Stock.fetchStock(ids+"_"+i.getName()){
                            sto->
                                if(sto==null||sto.getAvailableNo()<i.getTotal())
                                {
                                    btn.setBackgroundColor(Color.RED)
                                    btn.isEnabled=false

                                }
                        }
                    }

                    btn.setOnClickListener(View.OnClickListener {
                        btn.setBackgroundColor(Color.GREEN)
                        btn.isEnabled=false
                        for(i in ps.getDrugs())
                        {
                            Stock.fetchStock(ids+"_"+i.getName()){
                                    sto->
                                    if(sto!=null&&sto.getAvailableNo()>=i.getTotal())
                                     {
                                        sto.setAvailableNo(sto.getAvailableNo()-i.getTotal())
                                         sto.addToDatabase()
                                    }
                            }
                        }
                        ps.setFulfilledBy(ids);
                        ps.addToFirebase()
                        Toast.makeText(applicationContext,"Fulfilled Successfully",Toast.LENGTH_SHORT)
                        finish()
                    })


                }
            }
        }



    }










    fun feedUser(user: User): LinearLayout
    {
        val parentLinearLayout = LinearLayout(applicationContext)
        parentLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        parentLinearLayout.orientation = LinearLayout.VERTICAL
        parentLinearLayout.setPadding(20, 20, 20, 20)

        val childLinearLayout = LinearLayout(applicationContext)
        childLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        childLinearLayout.orientation = LinearLayout.HORIZONTAL
        childLinearLayout.setPadding(16, 16, 16, 16)
        childLinearLayout.elevation = 4f
        //childLinearLayout.outlineProvider = BackgroundOutlineProvider()
        childLinearLayout.background = ColorDrawable(Color.parseColor("#FAFAFA"))

        val imageView = ImageView(applicationContext)
        imageView.layoutParams = LinearLayout.LayoutParams(
            100,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.iconnny)
        imageView.foregroundGravity= Gravity.CENTER
        applicationContext?.let { user.getProfileImage(it, imageView) }
        imageView.setPadding(0, 0, 12, 0)
        imageView.clipToOutline = true
        imageView.background = applicationContext?.let { ContextCompat.getDrawable(it, R.drawable.rounded_corner_background) }
        imageView.setPadding(20,18,20,18)


        val textLinearLayout = LinearLayout(applicationContext)
        textLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textLinearLayout.orientation = LinearLayout.VERTICAL
        textLinearLayout.setPadding(50, 0, 0, 0)

        val nameTextView = TextView(applicationContext)
        nameTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        nameTextView.text = user.getFullName()


        val emailTextView = TextView(applicationContext)
        emailTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        emailTextView.text = user.getEmail();

        textLinearLayout.addView(nameTextView)
        //textLinearLayout.addView(emailTextView)
        val tmplay= LinearLayout(applicationContext)
        tmplay.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tmplay.orientation= LinearLayout.HORIZONTAL

        var doc= TextView(applicationContext)
        var i=user as Doctor
        doc.text=i.getMedicalPin()
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

    fun feedPrescription(prescription: Prescription):LinearLayout
    {
        // Create the outer LinearLayout
        val outerLayout = LinearLayout(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
            setPadding(20, 20, 20, 20)
        }

        // Create the first LinearLayout with 3 TextViews
        val firstLinearLayout = LinearLayout(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
        }

        val firstTextView1 = TextView(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            text = "# "+prescription.getUniqueId()
            setTextColor(Color.BLACK)
        }

        val firstTextView2 = TextView(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            text = prescription.name
            setTextColor(Color.BLACK)
        }

        val firstTextView3 = TextView(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            text = if(prescription.getFulfilledBy().isNotEmpty())"Fulfilled" else "Pending"
            setTextColor(Color.BLACK)
        }

        firstLinearLayout.addView(firstTextView1)
        firstLinearLayout.addView(firstTextView2)
        firstLinearLayout.addView(firstTextView3)

        // Create the second LinearLayout with 3 TextViews
        val secondLinearLayout = LinearLayout(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
        }

        val secondTextView1 = TextView(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            text = prescription.gender
            setTextColor(Color.BLACK)
        }

        val secondTextView2 = TextView(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            text = prescription.age.toString()+" yrs"
            setTextColor(Color.BLACK)
        }

        val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val secondTextView3 = TextView(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            text =sdf.format( prescription.getTime())
            setTextColor(Color.BLACK)
        }

        secondLinearLayout.addView(secondTextView1)
        secondLinearLayout.addView(secondTextView2)
        secondLinearLayout.addView(secondTextView3)



        outerLayout.addView(firstLinearLayout)
        outerLayout.addView(secondLinearLayout)

        outerLayout.addView(Space(applicationContext).apply {
            layoutParams = LinearLayout.LayoutParams(100, 20)
        })
        for(drug in prescription.getDrugs())
        {

            // Create the third LinearLayout with 3 TextViews and a Space view
            val thirdLinearLayout = LinearLayout(applicationContext).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
            }

            val spaceView = Space(applicationContext).apply {
                layoutParams = LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            val thirdTextView1 = TextView(applicationContext).apply {
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                text = drug.getName()
                setTextColor(Color.BLACK)
            }

            val thirdTextView2 = TextView(applicationContext).apply {
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                text = drug.getDosage().toString()+" X "+drug.getTimes().toString()
                setTextColor(Color.BLACK)
            }

            val thirdTextView3 = TextView(applicationContext).apply {
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                text = drug.getTotal().toString()
                setTextColor(Color.BLACK)
            }

            thirdLinearLayout.addView(Space(applicationContext).apply {
                layoutParams = LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT)
            })
            thirdLinearLayout.addView(thirdTextView1)
            thirdLinearLayout.addView(thirdTextView2)
            thirdLinearLayout.addView(thirdTextView3)



            outerLayout.addView(thirdLinearLayout)
        }




        return  outerLayout
    }

}