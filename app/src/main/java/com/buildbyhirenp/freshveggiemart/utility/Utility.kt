package com.buildbyhirenp.freshveggiemart.utility

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import com.buildbyhirenp.freshveggiemart.databinding.ProgressDialogBinding
import com.buildbyhirenp.freshveggiemart.utility.constants.PREFS_NAME
import com.google.firebase.auth.FirebaseAuth

class Utility {

    var mActivity : Activity

    constructor(mActivity: Activity) {
        this.mActivity = mActivity
    }

    companion object{
        private var dialog : AlertDialog? = null

        fun showToast(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showDialog(context: Context, message : String){
            val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))

            progress.progressDialogText.setText(message)
            dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
            dialog!!.show()
        }

        fun hideDialog(){
            dialog?.dismiss()
        }

    }
}