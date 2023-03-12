package com.tunha



import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

import com.bumptech.glide.request.transition.Transition as GlideTransition



open class User {
    private var fullName: String
    private var email: String
    private var password: String
    private var userType:String
    private var id: String

    public constructor()
    {
        this.fullName = ""
        this.email = ""
        this.password=""
        this.id = ""
        this.userType=""
    }


    constructor( email: String,fullName: String, password: String,type:String, id: String = UUID.randomUUID().toString()) {
        this.fullName = fullName
        this.email = email
        this.password=""
        this.id = id
        this.userType=type
        setRawPassword(password)
    }

    fun getFullName(): String {
        return this.fullName
    }

    fun setFullName(fullName: String) {
        this.fullName = fullName
    }

    fun getEmail(): String {
        return this.email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getPassword(): String {
        return this.password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getId(): String {
        return this.id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getUserType(): String {
        return this.userType
    }

    fun setUserType(userType: String) {
        this.userType = userType
    }

    fun addToFirebase() {
        val database = Firebase.database
        val myRef: DatabaseReference = database.getReference("users")

        myRef.child(this.getId()).setValue(this)
       // myRef.push().setValue(this)
    }

    fun hashPassword(password: String): String {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val hash = md.digest(password.toByteArray())

            val sb = StringBuilder()
            for (b in hash) {
                sb.append(String.format("%02x", b))
            }

            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }

        return ""
    }

    fun login(password: String): Boolean {
        // Get the stored hashed password for the given email from your database
        val storedHashedPassword = getPassword()

        // Hash the password entered by the user
        val hashedPassword = hashPassword(password)

        // Compare the two hashes
        return (hashedPassword == storedHashedPassword)

    }

    fun setRawPassword(password:String){
        this.password=hashPassword(password)
    }

    fun setProfileImage(context: Context, bitmap: Bitmap) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("profileImages/user${this.getId()}.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->

            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    fun getProfileImage(context: Context, imageView: ImageView) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("profileImages/user${this.getId()}.jpg")
        imagesRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        glideTransition: GlideTransition<in Drawable>?
                    ) {
                        imageView.setImageDrawable(resource)
                        TransitionManager.beginDelayedTransition(imageView.parent as ViewGroup)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // do nothing
                    }
                })
        }.addOnFailureListener {
            // if the user's profile image doesn't exist, load the default image from Firebase Storage
            val defaultImageRef = storageRef.child("defaultprofile.jpg")
            defaultImageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to get image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        @JvmStatic // Optional annotation to make the function callable from Java code
        fun fetchUsersFromDatabase(task: (List<User>) -> Unit) {
            val database = Firebase.database
            val databaseRef: DatabaseReference = database.getReference("users")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userList = mutableListOf<User>()
                    for (userSnapshot in dataSnapshot.children) {
                        var us: User? = userSnapshot.getValue(User::class.java)
                        if (us != null) {
                            Log.d(TAG,"list gotten"+userSnapshot)
                            userList.add(us)
                        }
                    }
                    task(userList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }


        fun fetchUserByIdFromDatabase(userId: String, task: (User?) -> Unit) {
            val database = Firebase.database
            val databaseRef: DatabaseReference = database.getReference("users").child(userId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: User? = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        val userType = user.getUserType()
                        when (userType) {
                            "Admin" -> {
                                val admin: Admin? = dataSnapshot.getValue(Admin::class.java)
                                task(admin)
                            }
                            "Doctor" -> {
                                val doctor: Doctor? = dataSnapshot.getValue(Doctor::class.java)
                                task(doctor)
                            }
                            "DrugSeller" -> {
                                val drugSeller: DrugSeller? = dataSnapshot.getValue(DrugSeller::class.java)
                                task(drugSeller)
                            }
                            else -> {
                                // Handle unknown user type
                                task(null)
                            }
                        }
                    } else {
                        task(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    task(null)
                }
            })
        }


    }
    // Rest of the User class implementation







}
