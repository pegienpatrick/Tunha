package com.tunha.ui.admins

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tunha.R
import com.tunha.Session
import com.tunha.User

class Profile : Fragment() {

    companion object {
        fun newInstance() = Profile()
    }

    private lateinit var viewModel: ProfileViewModel
    private var myuser:User?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_profile, container, false)
        // Inflate the fragment layout
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)




        feedData(rootView)

        return  rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 123
    fun feedData(rootView:View){
        val dpImageView: ImageView = rootView.findViewById(R.id.dp)
        val firstNameTextView: TextView = rootView.findViewById(R.id.fname)
        val emailTextView: TextView = rootView.findViewById(R.id.email)
        val changeProfilePictureButton: Button = rootView.findViewById(R.id.change_profile_picture)


        val paddingValue=20
        firstNameTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)
        emailTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

        val userid:String?= context?.let { Session.getUserSession(it).first }
        if (userid != null) {
            User.fetchUserByIdFromDatabase(userid ){user->
                if(user!=null) {
                    myuser=user
                    firstNameTextView.text = user.getFullName()
                    emailTextView.text = user.getEmail()
                    context?.let { user.getProfileImage(it,dpImageView) }
                    feedData(rootView)

                    changeProfilePictureButton.setOnClickListener(View.OnClickListener {
                        val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
                        if (activity?.let { it1 -> ContextCompat.checkSelfPermission(it1, permission) } == PackageManager.PERMISSION_GRANTED) {
                            // Permission is granted, do something
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
                        } else {
                            // Permission is not granted, request it
                            activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(permission), PERMISSION_REQUEST_CODE) }
                        }
                    })
                }


            }
        }



    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data // retrieve the selected image URI

            // Retrieve the file name associated with the selected image URI
            val contentResolver = context?.contentResolver


            // Load the image into a Bitmap using an InputStream
            val inputStream = selectedImageUri?.let { contentResolver?.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)

            context?.let { myuser?.setProfileImage(it,bitmap) }

            // do something with the Bitmap, such as displaying it in an ImageView
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the grantResults array is empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, do something
                } else {
                    // Permission is denied, handle the denied permission
                }
                return
            }
            else -> {
                // Handle other permission requests
            }
        }
    }

}