package com.zenora.elemento.common.baseclass


import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zenora.elemento.R
import com.google.android.material.snackbar.Snackbar


/**
 * Base class for all fragments with common functions added.
 */

open class BaseFragment : Fragment() {

    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressDialog()
    }

    override fun onDestroy() {
        dismissProgress()
        super.onDestroy()
    }

    /**
     * Initialize the progress dialog
     */
    private fun initProgressDialog() {
        try {
            activity?.let {
                progressDialog = Dialog(it)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(true)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(0))
                progressDialog.setContentView(R.layout.progress_dialog_common)
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgress() {
        try {
            if (progressDialog != null && !progressDialog.isShowing) {
                progressDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun dismissProgress() {
        try {
            if (progressDialog != null && progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSnackBarAlert(message: String?) {
        activity?.window?.decorView?.let {
            val messageText = message ?: resources.getString(R.string.common_error_message)
            val snackBar = Snackbar.make(
                it.findViewById(android.R.id.content),
                messageText,
                Snackbar.LENGTH_LONG
            )
            val snackBarView: View = snackBar.view
            val textView = snackBarView.findViewById(R.id.snackbar_text) as TextView
            textView.maxLines = 5
            snackBar.show()
        }

    }

    fun showLongToast(message: String?) {
        activity.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    fun showShortToast(message: String?) {
        activity.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addFragmentToLayout(layoutId: Int, fragment: Fragment) {
        try {
            val manager = childFragmentManager
            val trans = manager.beginTransaction()
            trans.replace(layoutId, fragment).commitAllowingStateLoss()
        } catch (e: Exception) {
            Log.e("BaseFragment", e.message.toString())
        }
    }
}
