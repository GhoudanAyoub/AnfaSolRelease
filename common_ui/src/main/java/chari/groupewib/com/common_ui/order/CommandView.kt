package ghoudan.anfaSolution.com.common_ui.order

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import ghoudan.anfaSolution.com.app_models.CommandAchat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.OrderViewBinding
import ghoudan.anfaSolution.com.utils.LocaleHelper

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CommandView : CardView {

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
    fun bind(data: CommandViewData) {

        val (command, listener) = data

        binding.orderCounter.text = LocaleHelper.replaceArabicNumbers(
            resources.getString(
                R.string.order_date,
                command.No.toString()
            )
        )
        binding.orderNum.text = LocaleHelper.replaceArabicNumbers(
            resources.getString(
                R.string.price,
                command.totalAmountTTC
            )
        )
        binding.orderPriceValue.text = command.description
        binding.clientName.text = StringBuilder().append(
            "code client: "
        ).append(command.customerId.toString())
        binding.clientNum.text =command.customerName
        binding.clientRef.text = StringBuilder().append(
            "code vendeur: "
        ).append(command.salerCode.toString())

        binding.articlesSize.text = command.Status
        binding.orderDate.text = command.desiredDeliveryDate


        binding.btnOrder.apply {
            setOnClickListener {
                listener.onDeleteClicked(command)
            }
        }
        binding.root.setOnClickListener {
            listener.onCommandClicked(command)
        }
    }

    data class CommandViewData(
        val command: CommandAchat,
        val listener: CommandListener
    )
}
