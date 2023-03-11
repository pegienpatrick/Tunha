package com.tunha

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class Doctor(email: String, fullName: String, password: String,
             private var medicalPin: String, id: String = UUID.randomUUID().toString()) : User(email, fullName, password, "Doctor", id) {

    constructor() : this("", "", "", "", "") {
        medicalPin = ""
    }

    private var approved:Boolean = false

    fun getMedicalPin():String
    {
        return this.medicalPin
    }

    fun setMedicalPin(pin:String){
        this.medicalPin=pin
    }


    fun setCertImage(context: Context, bitmap: Bitmap) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("certImages/user${this.getId()}.jpg")
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

    fun getCertImage(context: Context, imageView: ImageView) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("certImages/user${this.getId()}.jpg")
        imagesRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        glideTransition: Transition<in Drawable>?
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

                Toast.makeText(context, "Failed to get image", Toast.LENGTH_SHORT).show()

        }
    }

    // Getter method for the approved property
    fun isApproved(): Boolean {
        return approved
    }

    // Setter method for the approved property
    fun setApproved(value: Boolean) {
        approved = value
    }


}
