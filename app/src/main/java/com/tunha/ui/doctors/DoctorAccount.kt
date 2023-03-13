package com.tunha.ui.doctors

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.tunha.Doctor
import com.tunha.R
import com.tunha.Session
import com.tunha.User

import com.tunha.databinding.ActivityDoctorAccountBinding

class DoctorAccount : AppCompatActivity() {
    private var user: User? = null
    private lateinit var binding: ActivityDoctorAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)



        var ids:String= Session.getUserSession(this).first.toString()
        Log.d(TAG,"User was $ids")

        User.fetchUserByIdFromDatabase(ids){
            user ->
            var doc=user as Doctor
            if(doc.isApproved())
            {






        binding = ActivityDoctorAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navViewDoctors


        val navController = findNavController(R.id.nav_host_fragment_activity_doctor_account)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_prescriptions -> {
                    navController.navigate(R.id.doctorListPrescriptions)
                    true
                }
                R.id.navigation_prescribe -> {
                    navController.navigate(R.id.doctorAddPrescription)
                    true
                }
                R.id.navigation_notifications_doctors -> {
                    navController.navigate(R.id.notifications_doctors)
                    true
                }
                R.id.navigation_profile_doctors -> {
                    navController.navigate(R.id.profile_doctors)
                    true
                }
                else -> false
            }
        }

// Set the selected menu item to the first item in the menu
        navView.selectedItemId = R.id.navigation_prescriptions

        
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.doctorListPrescriptions,
                R.id.doctorAddPrescription,
                R.id.notifications_doctors,
                R.id.profile_doctors

            )

        )



        setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
            }
            else
            {
                setContentView(R.layout.notapproved)
            }
        }

    val context=this



    }

    override fun onBackPressed() {
        preventBackPressed("Are you sure you want to Logout from the app?")
    }


    fun Activity.preventBackPressed(prompt: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage(prompt)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                // do nothing
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finishAffinity()

            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                // do nothing
            }
            .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
}