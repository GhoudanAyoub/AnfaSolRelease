package ghoudan.anfaSolution.com.common_ui.order

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.OrderViewBinding
import ghoudan.anfaSolution.com.common_ui.order.data.OrderViewData
import ghoudan.anfaSolution.com.utils.LocaleHelper
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import java.text.SimpleDateFormat
import java.util.Locale

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OrderView : CardView {

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
    fun bind(data: OrderViewData) {

        val (order, listener) = data

        binding.orderNum.text = LocaleHelper.replaceArabicNumbers(
            resources.getString(
                R.string.order_number,
                order.orderId
            )
        )

        binding.orderDate.text = order.confirmedAt?.let { orderDate ->
            LocaleHelper.replaceArabicNumbers(
                resources.getString(
                    R.string.order_date,
                    SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(orderDate)
                )
            )
        }

//        when (order.orderStatusId) {
//            OrderStatus.LIVRE.intValue -> {
//                binding.btnOrderAgain.isEnabled = true
//                binding.orderStatusValue.apply {
//                    setText(resources.getString(OrderStatus.LIVRE.text))
//                    setState(OrderStatus.LIVRE.intValue)
//                }
//            }
//            OrderStatus.ANNULE.intValue -> {
//                binding.btnOrderAgain.isEnabled = false
//                binding.orderStatusValue.apply {
//                    setText(resources.getString(OrderStatus.ANNULE.text))
//                    setState(OrderStatus.ANNULE.intValue)
//                }
//            }
//            OrderStatus.CONFIRME.intValue -> {
//                binding.btnOrderAgain.isEnabled = false
//                binding.orderStatusValue.apply {
//                    setText(resources.getString(OrderStatus.CONFIRME.text))
//                    setState(OrderStatus.CONFIRME.intValue)
//                }
//            }
//            OrderStatus.RETOURNE.intValue -> {
//                binding.btnOrderAgain.isEnabled = false
//                binding.orderStatusValue.apply {
//                    setText(resources.getString(OrderStatus.RETOURNE.text))
//                    setState(OrderStatus.RETOURNE.intValue)
//                }
//            }
//            else -> {
//                binding.btnOrderAgain.isEnabled = false
//            }
//        }
//        binding.orderNumLines.text =
//            if (order.orderLines?.size == 1)
//                resources.getString(R.string.single_article_label)
//            else StringBuilder().append(
//                LocaleHelper.replaceArabicNumbers(
//                    resources.getString(
//                        R.string.order_num_lines,
//                        order.orderLines?.size
//                    )
//                )
//            ).append(
//                " " +
//                        resources.getString(
//                            R.string.multiple_articles_label
//                        )
//            )
        binding.orderPriceValue.text = LocaleHelper.replaceArabicNumbers(
            resources.getString(
                R.string.price,
                order.totalAmountTTC
            )
        )

        setOnClickListener {
            listener.onOrderClicked(order)
        }
    }
}
