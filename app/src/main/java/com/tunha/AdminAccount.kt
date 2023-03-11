package com.tunha

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData.Item
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.tunha.databinding.ActivityAdminAccountBinding
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import android.graphics.drawable.BitmapDrawable


class AdminAccount : AppCompatActivity() {
    private var user: User? = null
    private lateinit var binding: ActivityAdminAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)



        var ids:String=Session.getUserSession(this).first.toString()
        Log.d(TAG,"User was $ids")





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

    val context=this

        setUser()
        //setDp()

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

    fun setDp(){
        val context=this
        val imageView=ImageView(context)

        val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        val profileMenuItem = navigationView.menu.findItem(R.id.navigation_profile)

        //setUser()






        val storageRef = FirebaseStorage.getInstance().reference
        var imagesRef = storageRef.child("profileImages/"+"user${user?.getId()}.jpg")
        //imagesRef.child("user${user?.getId()}.jpeg")
        //imagesRef=storageRef.child("user${user?.getId()}.jpg")
       // Log.d(TAG,"/user${user?.getId()}.jpg")
        imagesRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        glideTransition: Transition<in Drawable>?
                    ) {
                        profileMenuItem.icon=resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // do nothing
                    }
                })
        }.addOnFailureListener {
            // if the user's profile image doesn't exist, load the default image from Firebase Storage
            //val defaultImageRef = storageRef.child("defaultprofile.jpg")
            val defaultImageRef=storageRef.child("user1.jpg")
            defaultImageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            glideTransition: Transition<in Drawable>?
                        ) {

                            profileMenuItem.icon=resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // do nothing
                        }
                    })
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to get image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setUser(){

        val auth = FirebaseAuth.getInstance()

        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User is signed in anonymously
                    Log.d(TAG,"Sucess")
                } else {
                    // Sign in failed
                    Log.d(TAG,"Auth failed")
                }
            }




        var ids:String=Session.getUserSession(this).first.toString()
        val database = Firebase.database
        val myRef = database.getReference("users")

        // Query data where the "userId" is equal to the given userId parameter
        val query = myRef.child(ids)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = if (dataSnapshot.exists()) {
                    dataSnapshot.getValue(User::class.java)
                } else {
                    null
                }
                if (dataSnapshot.exists()) {
                    Log.d(TAG,"\n\nuser  \n\n")
                    Log.d(TAG, user.toString())
                    //setDp()
                }
                else
                {
                    Log.d(TAG,"\n\nFailed to see user\n\n")
                }
                //continuation.resume(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error getting user with id $ids: ${databaseError.message}")
                //continuation.resume(null)
            }
        })
    }




}



