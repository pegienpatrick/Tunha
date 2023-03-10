package com.tunha

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData.Item
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.tunha.databinding.ActivityAdminAccountBinding
import java.util.concurrent.CompletableFuture

class AdminAccount : AppCompatActivity() {
    private var user: User? = null
    private lateinit var binding: ActivityAdminAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)



        var ids:String=Session.getUserSession(this).first.toString()
        Log.d(TAG,"User was $ids")
        user=Session.getUser(ids)




        binding = ActivityAdminAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_admin_account)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_notifications,

                R.id.navigation_admins,
                R.id.navigation_distributors,
                R.id.navigation_doctors,
                R.id.navigation_profile
            )

        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        //run {
            //val navigationView = findViewById<NavigationView>(R.id.nav_view)
            //val profileMenuItem = navigationView.menu.findItem(R.id.navigation_profile)

            val imageView = ImageView(this)
            Log.d(TAG, "User is  $user", null)

//            try{
                val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
                val profileMenuItem = navigationView.menu.findItem(R.id.navigation_profile)

                user?.getProfileImage(this, imageView)

                imageView.drawable?.let {
                    Log.d(TAG,"found")
                    profileMenuItem.icon = it
                }
//            }catch (exception:Exception)
//            {
//                exception.printStackTrace()
//            }


       // }

    }

    override fun onBackPressed() {
        preventBackPressed("Are you sure you want to exit the app?")
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

private fun <T> CompletableFuture<T>.get(onError: () -> Unit?): User? {
    try {
        // Get the result of the CompletableFuture
        val result = this.get()
        // If the result is a User, return it
        if (result is User) {
            return result
        }
    } catch (e: Exception) {
        // If the CompletableFuture throws an exception, call the onError lambda
        onError?.invoke()
    }
    // If the result is not a User or the CompletableFuture fails, return null
    return null
}

