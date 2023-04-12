package ghoudan.anfaSolution.com.common_ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import ghoudan.anfaSolution.com.common_ui.databinding.FragmentPopupLoaderBinding

class LoadingAlertDialog(
    context: Context,
    val data: LoadingAlertDialogData,
    @StyleRes var styleResId: Int, var isRtl: Boolean
) : AlertDialog(context, styleResId) {


    private lateinit var binding: FragmentPopupLoaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPopupLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadingLabel.text = data.msg
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (isRtl)
            window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_RTL
        setCancelable(false)
    }

    data class LoadingAlertDialogData(val msg: String)
}
