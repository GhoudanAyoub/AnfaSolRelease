package ghoudan.anfaSolution.com.common_ui.product

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.app_models.ItemStock
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import ghoudan.anfaSolution.com.common_ui.databinding.ProductOrderViewBinding
import ghoudan.anfaSolution.com.common_ui.order.data.ProductOrderViewData
import ghoudan.anfaSolution.com.utils.LocaleHelper

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductOrderView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ProductOrderViewBinding.inflate(LayoutInflater.from(context), this)

    @ModelProp
    fun bind(data: ProductOrderViewData) {

        val (orderLine, listener) = data

        if (orderLine is Item) {
            binding.productName.text =
                orderLine.code.toString()
            binding.productDescription.text =
                orderLine.description

            binding.blocked.isVisible = orderLine.Blocked == true
            binding.itemUnits.text = "prix unitaire: ${
                LocaleHelper.replaceArabicNumbers(
                    orderLine.unitCost.toString()
                )
            }"
            binding.itemWeight.text = "unité de base: ${orderLine.unitCode}"
            binding.itemQnt.apply {
                text = StringBuilder().append(
                    "Quantity: "
                ).append(
                    LocaleHelper.replaceArabicNumbers(
                        orderLine.quantity.toString()
                    )
                )
                isVisible = orderLine.quantity.toString().isNotEmpty()
            }
            binding.itemQnt.isVisible = !orderLine.hideUnits
            binding.delete.isVisible = !orderLine.hideUnits
            binding.root.setOnClickListener {
                listener.onItemClicked(orderLine)
            }
            binding.delete.setOnClickListener {
                listener.onItemDeleted(orderLine)
            }
        } else if (orderLine is ItemStock) {
            binding.productName.text =
                orderLine.code.toString()
            binding.docNum.apply {
                text =
                    orderLine.Document_No.toString()
                isVisible = orderLine.Document_No.toString().isNotEmpty()
            }
            binding.productFamily.apply {
                text =
                    orderLine.family.toString()
                isVisible = orderLine.family.toString().isNotEmpty()
            }
            binding.productDescription.text =
                orderLine.description
            binding.delete.isVisible = false
            binding.itemWeight.isVisible = false
            binding.itemUnits.text = "unité de base : " + orderLine.unitCode
            binding.itemQnt.apply {
                text = StringBuilder().append(
                    "Quantity: "
                ).append(
                    LocaleHelper.replaceArabicNumbers(
                        orderLine.quantity.toString()
                    )
                )
                isVisible = orderLine.quantity.toString().isNotEmpty()
            }
        }
    }
}
