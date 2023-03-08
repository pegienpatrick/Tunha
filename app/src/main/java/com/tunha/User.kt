package com.tunha



import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*



open class User {
    private var fullName: String
    private var email: String
    private var password: String
    private var userType:String
    private var id: String

    constructor()
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




}
