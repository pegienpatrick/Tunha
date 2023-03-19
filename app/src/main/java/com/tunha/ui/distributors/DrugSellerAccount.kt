package com.tunha.ui.distributors

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.tunha.*
import com.tunha.databinding.ActivityDrugSellerAccountBinding


class DrugSellerAccount : AppCompatActivity() {
	private lateinit var binding: ActivityDrugSellerAccountBinding
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)






        var ids:String= Session.getUserSession(this).first.toString()
        Log.d(TAG,"User was $ids")

        User.fetchUserByIdFromDatabase(ids){
                user ->
            var doc=user as DrugSeller
            if(doc.isApproved()) {


                binding = ActivityDrugSellerAccountBinding.inflate(layoutInflater)
                setContentView(binding.root)

                val navView: BottomNavigationView = binding.navViewDrugsellers

                val navController =
                    findNavController(R.id.nav_host_fragment_activity_drugseller_account)
                // Set up the navigation listener for the BottomNavigationView
                navView.setOnNavigationItemSelectedListener { menuItem ->
                    // Navigate to the appropriate destination based on the selected menu item
                    when (menuItem.itemId) {
                        R.id.navigation_sell_prescription -> {
                            navController?.navigate(R.id.chemist_fulfil_prescription)
                            true
                        }
                        R.id.navigation_sell -> {
                            navController?.navigate(R.id.fragmentChemistWholesale)
                            true
                        }
                        R.id.navigation_stock -> {
                            navController?.navigate(R.id.distributorStockManagement)
                            true
                        }
                        R.id.navigation_notifications_drugsellers -> {
                            navController?.navigate(R.id.notifications_drugsellers)
                            true
                        }
                        R.id.navigation_profile_drugsellers -> {
                            navController?.navigate(R.id.profile_drug_sellers)
                            true
                        }
                        else -> false
                    }
                }

// Set the selected menu item to the first item in the menu
                navView.selectedItemId = R.id.chemist_fulfil_prescription

                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_stock,
                        R.id.navigation_sell,
                        R.id.navigation_sell_prescription,
                        R.id.navigation_notifications_drugsellers,
                        R.id.navigation_profile_drugsellers
                    )
                )



                setupActionBarWithNavController(navController, appBarConfiguration)

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