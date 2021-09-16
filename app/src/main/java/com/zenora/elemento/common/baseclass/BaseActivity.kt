package com.zenora.elemento.common.baseclass

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zenora.elemento.R
import com.google.android.material.snackbar.Snackbar

/**
 * Base class for all activities with common functions added.
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressDialog()
    }

    /**
     * Initialize the progress dialog
     */
    private fun initProgressDialog() {
        try {
            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(0))
            progressDialog.setContentView(R.layout.progress_dialog_common)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgress() {
        try {
            if (!progressDialog.isShowing) {
                progressDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun dismissProgress() {
        try {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSnackBarAlert(message: String?) {
        val messageText = message ?: resources.getString(R.string.common_error_message)
        val snackBar = Snackbar.make(
            window.decorView.findViewById(android.R.id.content),
            messageText,
            Snackbar.LENGTH_LONG
        )
        val snackBarView: View = snackBar.view
        val textView = snackBarView.findViewById(R.id.snackbar_text) as TextView
        textView.maxLines = 5
        snackBar.show()
    }

    fun showLongToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
