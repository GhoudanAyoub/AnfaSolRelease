package ghoudan.anfaSolution.com.common_ui.order


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import ghoudan.anfaSolution.com.app_models.PurchaseOrder
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.OrderViewBinding
import ghoudan.anfaSolution.com.utils.LocaleHelper
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CommandSalesView : CardView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = OrderViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        elevation = 0f
        radius = 0f
    }

    @ModelProp
    fun bind(data: CommandSalesViewData) {

        val (command, listener) = data

        binding.orderCounter.text = LocaleHelper.replaceArabicNumbers(
            resources.getString(
                R.string.order_date,
                command.counter.toString()
            )
        )
        binding.orderNum.text = ""
        binding.orderPriceValue.text =
            LocaleHelper.replaceArabicNumbers(
                resources.getString(
                    R.string.order_date,
                    command.Pay_to_Vendor_No.toString()
                )
            )
        binding.clientName.text = command.Pay_to_Name.toString()
        binding.clientRef.text = command.Pay_to_Contact.toString()
        binding.orderDate.text = "Date de commande: ${command.desiredDeliveryDate}"
        binding.dueDate.text = "Date d'échéance: ${command.Due_Date}"
        binding.clientNum.text = "Date de réception demandée: ${command.requestReceiptDate}"


        binding.btnOrder.apply {
            setOnClickListener {
                listener.onSalesDeleteClicked(command)
            }
        }
        binding.root.setOnClickListener {
            listener.onSalesCommandClicked(command)
        }
    }

    data class CommandSalesViewData(
        val command: PurchaseOrder,
        val listener: CommandListener
    )
}
