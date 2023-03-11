package com.tunha

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class Session {



    companion object {
        private const val PREFERENCES_NAME = "user_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_TYPE = "user_type"

        fun saveUserSession(context: Context, userId: String, userType: String) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(KEY_USER_ID, userId)
            editor.putString(KEY_USER_TYPE, userType)
            editor.apply()
        }

        fun hasActiveSession(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString(KEY_USER_ID, null)
            val userType = sharedPreferences.getString(KEY_USER_TYPE, null)
            return (userId != null && userType != null)
        }

        fun getUserSession(context: Context): Pair<String?, String?> {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString(KEY_USER_ID, null)
            val userType = sharedPreferences.getString(KEY_USER_TYPE, null)
            return Pair(userId, userType)
        }

        fun clearUserSession(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        suspend fun getUser(userId: String): User? = suspendCoroutine { continuation ->
            val database = Firebase.database
            val myRef = database.getReference("users")

            // Query data where the "userId" is equal to the given userId parameter
            val query = myRef.child(userId)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = if (dataSnapshot.exists()) {
                        dataSnapshot.getValue(User::class.java)
                    } else {
                        null
                    }
                    continuation.resume(user)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error getting user with id $userId: ${databaseError.message}")
                    continuation.resume(null)
                }
            })
        }



    }
}


