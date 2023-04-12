package ghoudan.anfaSolution.com.common_ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.CustomCancelableDialogBinding
import ghoudan.anfaSolution.com.common_ui.databinding.CustomErrorDialogBinding
import ghoudan.anfaSolution.com.utils.Constants


object CustomDialog {

    fun inflateCustomDialogView(context: Context, title: String, body: String): View {
        val customErrorDialogViewBinding = ghoudan.anfaSolution.com.common_ui.databinding.CustomErrorDialogBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        customErrorDialogViewBinding.apply {
            titleMsg.text = title
            msgBody.text = body
            msgBody.gravity = Gravity.CENTER_HORIZONTAL
        }
        customErrorDialogViewBinding.titleMsg.text = title
        customErrorDialogViewBinding.msgBody.text = body

        return customErrorDialogViewBinding.root
    }

    fun inflateCancelableCustomDialogView(context: Context, title: String, body: String): View {
        val customCancelableDialogBinding = ghoudan.anfaSolution.com.common_ui.databinding.CustomCancelableDialogBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        customCancelableDialogBinding.apply {
            titleMsg.text = title
            msgBody.text = body
            msgBody.gravity = Gravity.CENTER_HORIZONTAL
        }
        return customCancelableDialogBinding.root
    }

    fun showCustomAlertDialog(
        context: Context,
        customView: View,
        buttonText: Int = R.string.btn_ok,
        cancelable: Boolean = true,
        function: (() -> Unit)? = null
    ) {
        val alertDialog: AlertDialog.Builder =
            AlertDialog.Builder(context)
        alertDialog.setView(customView)
            .setCancelable(cancelable)
            .setPositiveButton(context.resources.getString(buttonText)) { dialog: DialogInterface, _: Int ->
                function?.let {
                    function.invoke()
                }
                dialog.cancel()
            }

            .create()
        alertDialog.show().withCenteredButton()
    }

    fun showCustomCancelableAlertDialog(
        context: Context,
        customView: View,
        function: (() -> Unit)? = null
    ) {
        val alertDialog =
            AlertDialog.Builder(context)
        alertDialog
            .setView(customView)
            .setCancelable(true)
            .setPositiveButton(context.resources.getString(R.string.btn_confirm)) { dialog: DialogInterface, _: Int ->
                function?.let {
                    function.invoke()
                }
                dialog.dismiss()
            }
            .setNegativeButton(context.resources.getString(R.string.btn_cancel)) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show().withCenteredButton()
    }

    private fun AlertDialog.withCenteredButton() {
        val positiveButton = getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = getButton(AlertDialog.BUTTON_NEGATIVE)

        val positiveButtonParent = positiveButton.parent as? LinearLayout
        positiveButtonParent?.gravity = Gravity.CENTER_HORIZONTAL

        val negativeButtonParent = negativeButton.parent as? LinearLayout
        negativeButtonParent?.gravity = Gravity.CENTER_HORIZONTAL

        val leftSpacer = positiveButtonParent?.getChildAt(1)
        leftSpacer?.visibility = View.GONE

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.weight = 1f
        layoutParams.gravity = Gravity.CENTER
        val textFontFamily = ResourcesCompat.getFont(context, R.font.poppins_medium)

        positiveButton.layoutParams = layoutParams
        positiveButton.setTextColor(ContextCompat.getColor(context, R.color.accent_color))
        positiveButton.textSize = 16F
        positiveButton.typeface = textFontFamily
        positiveButton.isAllCaps = false

        negativeButton.layoutParams = layoutParams
        negativeButton.setTextColor(ContextCompat.getColor(context, R.color.accent_color))
        negativeButton.textSize = 16F
        negativeButton.typeface = textFontFamily
        negativeButton.isAllCaps = false
    }

    private fun isWhatsappInstalled(activity: Activity): Boolean {
        val pm: PackageManager = activity.packageManager
        return try {
            pm.getPackageInfo(Constants.WHATSAPP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
