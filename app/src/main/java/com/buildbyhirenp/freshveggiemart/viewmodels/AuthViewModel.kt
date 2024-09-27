package com.buildbyhirenp.freshveggiemart.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.buildbyhirenp.freshveggiemart.models.Users
import com.buildbyhirenp.freshveggiemart.utility.FirebaseUtils
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val _VerificationID = MutableStateFlow<String?>(null)

    private val _otpsend = MutableStateFlow<Boolean>(false)
    val otpsend = _otpsend

    private val _isSignInSuccessfully = MutableStateFlow(false)
    val isSignInSuccessfully = _isSignInSuccessfully

    fun sendOTP(phonenumber : String, activity : Activity){

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted:$credential")

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("TAG", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken, ) {
                Log.d("TAG", "onCodeSent:$verificationId")

                _VerificationID.value = verificationId
                _otpsend.value = true
            }
        }

        val options = PhoneAuthOptions.newBuilder(FirebaseUtils.getAuthInstance())
            .setPhoneNumber(phonenumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, Phonenumber: String, context: Context) {
        val credential = PhoneAuthProvider.getCredential(_VerificationID.value.toString(), otp)
        FirebaseUtils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
//                    FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(user.uid!!).setValue(user)
                    _isSignInSuccessfully.value = true
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Utility.hideDialog()
                        Utility.showToast(context, "Invalid OTP")
                    }
                    // Update UI
                }
            }
    }
}