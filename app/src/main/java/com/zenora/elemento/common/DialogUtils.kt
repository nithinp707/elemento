@file:Suppress("unused")

package com.zenora.elemento.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.zenora.elemento.R
import com.zenora.elemento.common.helper.DebouncedOnClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * This class manages the Alert Dialogs for application
 */
class DialogUtils {

    //To show information dialog
    fun showInfoDialog(
        context: Context?, dialogTitle: String?, dialogMessage: String?,
        dialogBtn: String?, infoDialogClickListener: InfoDialogClickListener
    ) {
        MaterialAlertDialogBuilder(context!!, R.style.MaterialAlertDialog_Rounded)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(
                dialogBtn
            ) { dialog, which -> infoDialogClickListener.onPositiveBtnClick() }
            .show()
    }


    // To show list of option in a dialog
    fun showOptionsDialog(
        context: Context?, dialogTitle: String?, dialogOptions: Array<String?>?,
        optionsDialogClickListener: OptionsDialogClickListener
    ) {
        MaterialAlertDialogBuilder(context!!, R.style.MaterialAlertDialog_Rounded)
            .setTitle(dialogTitle)
            .setItems(dialogOptions) { dialog: DialogInterface?, which: Int ->
                optionsDialogClickListener.onOptionSelect(
                    which
                )
            }
            .show()
    }


    /**
     * Information Dialog Click Listener
     */
    interface InfoDialogClickListener {
        /**
         * Positive Button Click
         */
        fun onPositiveBtnClick()
    }


    /**
     * Options Dialog Click Listener
     */
    interface OptionsDialogClickListener {
        /**
         * Positive Button Click
         */
        fun onOptionSelect(mSelectedOption: Int)
    }


    /**
     * Confirmation Dialog Click Listener
     */
    interface ConfirmationDialogClickListener {
        /**
         * Positive Button Click
         */
        fun onPositiveBtnClick()

        /**
         * Negative Button Click
         */
        fun onNegativeBtnClick()
    }


    /**
     * Interface for User Selection ChatImage
     */
    interface UserSelection {
        fun onSelection(selection: String?)
    }


    interface GetDialogObject {
        fun onDialogueCreated(alertDialog: AlertDialog?)
    }


    //To show confirmation dialog
    fun showConfirmationDialog(
        context: Context?, dialogTitle: String?, dialogMessage: String?,
        dialogPositiveBtn: String?, dialogNegativeBtn: String?,
        confirmationDialogClickListener: ConfirmationDialogClickListener
    ) {
        MaterialAlertDialogBuilder(context!!, R.style.MaterialAlertDialog_Rounded)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(
                dialogPositiveBtn
            ) { dialog: DialogInterface?, which: Int -> confirmationDialogClickListener.onPositiveBtnClick() }
            .setNegativeButton(
                dialogNegativeBtn
            ) { dialog: DialogInterface?, which: Int -> confirmationDialogClickListener.onNegativeBtnClick() }
            .show()
    }

    //To show a dialog with single input field
    fun showSingleInputDialog(
        activity: Activity,
        inputText: String,
        buttonName: String?,
        dialogueTitle: String?,
        inputResponse: OnDialogInputResponse
    ) {
        // Create an alert builder
        val builder = AlertDialog.Builder(activity)
        // set the custom layout
        val alertLayout: View =
            activity.layoutInflater.inflate(R.layout.layout_single_input, null)
        builder.setView(alertLayout)
        // create and show the alert dialog
        val dialog = builder.create()
        val dialogTitle = alertLayout.findViewById<TextView>(R.id.title_custom_alert)
        val closeButton = alertLayout.findViewById<ImageView>(R.id.button_close)
        val textReasonLabel = alertLayout.findViewById<TextView>(R.id.label)
        val editTextComment = alertLayout.findViewById<EditText>(R.id.edtInput)
        val buttonSubmit = alertLayout.findViewById<TextView>(R.id.button_submit)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //set text with translation
        textReasonLabel.text = ""
        editTextComment.hint = ""
        dialogTitle.text = dialogueTitle
        buttonSubmit.text = buttonName

        //set font
        FontFactory.setTypeface(
            activity,
            dialogTitle,
            FontFactory.MEDIUM,
            R.dimen.text_size_normal
        )
        FontFactory.setTypeface(
            activity,
            textReasonLabel,
            FontFactory.REGULAR,
            R.dimen.text_size_xsmall
        )
        FontFactory.setTypeface(
            activity,
            editTextComment,
            FontFactory.REGULAR,
            R.dimen.text_size_normal
        )
        FontFactory.setTypeface(
            activity,
            buttonSubmit,
            FontFactory.MEDIUM,
            R.dimen.text_size_normal
        )
        if (inputText.isNotEmpty()) {
            editTextComment.setText(inputText)
        }
        editTextComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                //beforeTextChanged
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                if (charSequence != null && charSequence.isNotEmpty()) {
                    buttonSubmit.isEnabled = true
                    buttonSubmit.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.colorWhite
                        )
                    )
                } else {
                    buttonSubmit.isEnabled = false
                    buttonSubmit.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.colorAlertBackground
                        )
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {
                //afterTextChanged
            }
        })
        closeButton.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View?) {
                dialog.dismiss()
            }
        })
        buttonSubmit.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View?) {
                inputResponse.onInputResponse(
                    editTextComment.text.toString().trim { it <= ' ' })
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    //To show a dialog with text only
    fun showTextViewOnlyDialog(
        activity: Activity,
        title: String?,
        labelText: String,
        labelBottom: String
    ) {
        // Create an alert builder
        val builder = AlertDialog.Builder(activity)
        // set the custom layout
        val alertLayout: View =
            activity.layoutInflater.inflate(R.layout.layout_text_view_only, null)
        builder.setView(alertLayout)
        // create and show the alert dialog
        val dialog = builder.create()
        val dialogTitle = alertLayout.findViewById<TextView>(R.id.title_custom_alert)
        val txtLabelBottom = alertLayout.findViewById<TextView>(R.id.label_bottom)
        val closeButton = alertLayout.findViewById<ImageView>(R.id.button_close)
        val txtViewMessage = alertLayout.findViewById<TextView>(R.id.txt_message)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //set font
        FontFactory.setTypeface(
            activity,
            dialogTitle,
            FontFactory.MEDIUM,
            R.dimen.text_size_medium
        )
        FontFactory.setTypeface(
            activity,
            txtLabelBottom,
            FontFactory.REGULAR,
            R.dimen.text_size_small
        )
        FontFactory.setTypeface(
            activity,
            txtViewMessage,
            FontFactory.REGULAR,
            R.dimen.text_size_normal
        )
        dialogTitle.text = title
        txtViewMessage.text = labelText
        if (labelBottom.isNotEmpty()) {
            txtLabelBottom.text = labelBottom
            txtLabelBottom.visibility = View.VISIBLE
        }
        if (labelText.isNotEmpty()) {
            txtViewMessage.text = labelText
        }
        closeButton.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View?) {
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    //To show a dialog with a progress action
    fun showDialogWithProgress(
        activity: Activity,
        title: String?,
        labelText: String?,
        dialogueObject: GetDialogObject
    ) {
        // Create an alert builder
        val builder = AlertDialog.Builder(activity)
        // set the custom layout
        val alertLayout: View =
            activity.layoutInflater.inflate(R.layout.layout_progress_dialog, null)
        builder.setView(alertLayout)
        // create and show the alert dialog
        val dialog = builder.create()
        val dialogTitle = alertLayout.findViewById<TextView>(R.id.txt_title)
        val txtViewMessage = alertLayout.findViewById<TextView>(R.id.txt_message)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //set font
        FontFactory.setTypeface(
            activity,
            dialogTitle,
            FontFactory.MEDIUM,
            R.dimen.text_size_medium
        )
        FontFactory.setTypeface(
            activity,
            txtViewMessage,
            FontFactory.REGULAR,
            R.dimen.text_size_normal
        )
        dialogTitle.text = title
        txtViewMessage.text = labelText
        dialog.show()
        dialogueObject.onDialogueCreated(dialog)
    }

    // To show a dialog with ok button to confirm
    fun showDialogWithOkayButton(
        activity: Activity,
        title: String?,
        labelText: String?,
        buttonName: String?,
        confirmationDialogClickListener: ConfirmationDialogClickListener
    ) {
        // Create an alert builder
        val builder = AlertDialog.Builder(activity)
        // set the custom layout
        val alertLayout: View =
            activity.layoutInflater.inflate(R.layout.layout_custom_dialog_ok_button, null)
        builder.setView(alertLayout)
        // create and show the alert dialog
        val dialog = builder.create()
        val dialogTitle = alertLayout.findViewById<TextView>(R.id.txt_title)
        val txtViewMessage = alertLayout.findViewById<TextView>(R.id.txt_message)
        val txtViewOkButton = alertLayout.findViewById<TextView>(R.id.txt_button)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //set font
        FontFactory.setTypeface(
            activity,
            dialogTitle,
            FontFactory.BOLD,
            R.dimen.text_size_medium
        )
        FontFactory.setTypeface(
            activity,
            txtViewMessage,
            FontFactory.REGULAR,
            R.dimen.text_size_small
        )
        FontFactory.setTypeface(
            activity,
            txtViewOkButton,
            FontFactory.MEDIUM,
            R.dimen.text_size_medium
        )
        dialogTitle.text = title
        txtViewMessage.text = labelText
        txtViewOkButton.text = buttonName
        dialog.show()
        txtViewOkButton.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View?) {
                dialog.cancel()
                confirmationDialogClickListener.onPositiveBtnClick()
            }
        })
    }

    //To show a dialog with 2 button options
    fun showDialogPositiveNegativeButton(
        activity: Activity,
        title: String?,
        labelText: String?,
        positiveButtonName: String?,
        negativeButtonName: String?,
        confirmationDialogClickListener: ConfirmationDialogClickListener
    ) {
        // Create an alert builder
        val builder = AlertDialog.Builder(activity)
        // set the custom layout
        val alertLayout: View = activity.layoutInflater.inflate(
            R.layout.layout_custom_dialog_positive_negative,
            null
        )
        builder.setView(alertLayout)
        // create and show the alert dialog
        val dialog = builder.create()
        val dialogTitle = alertLayout.findViewById<TextView>(R.id.txt_title)
        val txtViewMessage = alertLayout.findViewById<TextView>(R.id.txt_message)
        val txtViewPositiveButton = alertLayout.findViewById<TextView>(R.id.txt_positive_button)
        val txtViewNegativeButton = alertLayout.findViewById<TextView>(R.id.txt_negative_button)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //set font
        FontFactory.setTypeface(
            activity,
            dialogTitle,
            FontFactory.BOLD,
            R.dimen.text_size_medium
        )
        FontFactory.setTypeface(
            activity,
            txtViewMessage,
            FontFactory.REGULAR,
            R.dimen.text_size_small
        )
        FontFactory.setTypeface(
            activity,
            txtViewPositiveButton,
            FontFactory.MEDIUM,
            R.dimen.text_size_medium
        )
        FontFactory.setTypeface(
            activity,
            txtViewNegativeButton,
            FontFactory.MEDIUM,
            R.dimen.text_size_medium
        )
        dialogTitle.text = title
        txtViewMessage.text = labelText
        txtViewPositiveButton.text = positiveButtonName
        txtViewNegativeButton.text = negativeButtonName
        dialog.show()
        txtViewPositiveButton.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View?) {
                dialog.cancel()
                confirmationDialogClickListener.onPositiveBtnClick()
            }
        })
        txtViewNegativeButton.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View?) {
                dialog.cancel()
                confirmationDialogClickListener.onNegativeBtnClick()
            }
        })
    }
}

interface OnDialogInputResponse {
    fun onInputResponse(inputResponse: String?)
}
