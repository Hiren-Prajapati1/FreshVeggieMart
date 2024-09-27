package com.buildbyhirenp.freshveggiemart.utility

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FirebaseUtils {

    companion object{
        private var firebaseAuthInstane : FirebaseAuth? = null

        fun getAuthInstance() : FirebaseAuth{
            if (firebaseAuthInstane == null){
                firebaseAuthInstane = FirebaseAuth.getInstance()
            }
            return firebaseAuthInstane!!
        }

        fun getCurrentUserID() : String?{
            return FirebaseAuth.getInstance().currentUser?.uid
        }

        fun currentUserDetails() : DatabaseReference? {
            return FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(getCurrentUserID()!!)
        }

        fun isLoggedIn() : Boolean{
            if (getCurrentUserID() != null){
                return true
            }
            return false
        }

        fun Logout() {
            FirebaseAuth.getInstance().signOut()
        }

        fun fetchProductPath(path: String) : DatabaseReference{
            return FirebaseDatabase.getInstance().getReference("Admins").child(path)
        }

        fun getRandomId() : String{
            return (1..25).map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }.joinToString("")
        }

        fun getCurrentDate() : String{
            val currentDate = LocalDate.now()
            val formmater = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            return currentDate.format(formmater)
        }
    }
}