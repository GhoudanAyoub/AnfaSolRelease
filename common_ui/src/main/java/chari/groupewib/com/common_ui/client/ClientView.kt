package ghoudan.anfaSolution.com.common_ui.client

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ClientDetailsViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ClientView : CardView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding = ClientDetailsViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        elevation = 0f
        radius =
            resources.getDimensionPixelSize(R.dimen.horizontal_collection_item_radius).toFloat()
    }

    @ModelProp
    fun bind(data: ClientViewData) {

        val (customer, listener) = data
        binding.clientBlocked.text = "Blocked: ${customer.Blocked}"
        if (customer.Blocked?.isEmpty() == true || customer.Blocked?.contains(" ") == true  ) {
            binding.clientBlocked.visibility = GONE
        }else
            binding.clientBlocked.visibility = VISIBLE
        if(customer.Blocked?.lowercase()?.contains("all")==true)
            binding.clientBlocked.setTextColor(resources.getColor(R.color.status_error))
        else
            binding.clientBlocked.setTextColor(resources.getColor(R.color.status_alert))

        binding.clientName.text = customer.name ?: "-"
        binding.clientPhone.text = customer.Phone_No ?: "-"

        setOnClickListener {
                listener.onCustomerClicked(customer)
        }

    }

    data class ClientViewData(
        val customer: CustomerAnfa,
        val clientViewListener: ClientViewListener
    )
}
