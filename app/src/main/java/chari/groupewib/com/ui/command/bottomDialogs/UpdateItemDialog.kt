package chari.groupewib.com.ui.command.bottomDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import chari.groupewib.com.app_models.Item
import ghoudan.anfaSolution.com.databinding.FragmentUpdateItemDialogBinding
import ghoudan.anfaSolution.com.utils.AppUtils

class UpdateItemDialog(
    context: Context?,
    val item: Item,
    val callback: (unitWeight: Double, itemUnits: Double, parcel: Int) -> Unit,
) : AlertDialog(context) {

    private lateinit var binding: FragmentUpdateItemDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUpdateItemDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (item.itemWeight != 0.0) item.itemWeight?.let { binding.requestedTotalWeight.setText(it.toString()) }
        if (item.itemUnits != 0.0) item.itemUnits?.let { binding.requestedTotalUnits.setText(it.toString()) }
        if (item.parcel != 0) item.parcel?.let { binding.requestedTotalParcel.setText(it.toString()) }

        binding.confirmOrderBtn.setOnClickListener {
            AppUtils.hideKeyboardWithView(context, binding.requestedTotalUnits)
            val weight = if (binding.requestedTotalWeight.text.toString()
                    .isNotEmpty()
            ) binding.requestedTotalWeight.text.toString().toDouble() else 0
            val units = if (binding.requestedTotalUnits.text.toString()
                    .isNotEmpty()
            ) binding.requestedTotalUnits.text.toString().toDouble() else 0
            val parcel = if (binding.requestedTotalParcel.text.toString()
                    .isNotEmpty()
            ) binding.requestedTotalParcel.text.toString().toDouble() else 0
            callback.invoke(
                weight.toDouble(),
                units.toDouble(),
                parcel.toInt()
            )
            dismiss()
        }
    }

}
