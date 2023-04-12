package ghoudan.anfaSolution.com.common_ui.product

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.app_models.OrderLine
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ProductCartViewBinding
import ghoudan.anfaSolution.com.utils.LocaleHelper
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductCartView : CardView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        elevation = 0f
        radius = 0f
    }

    private var articleQty = 0
    private var productListener: ProductListener? = null
    private val binding = ProductCartViewBinding.inflate(LayoutInflater.from(context), this)

    @ModelProp
    fun bind(data: ProductCartViewData) {

        val (orderLine, listener) = data
        productListener = listener
//
//        Glide.with(context)
//            .load(orderLine.article?.imageListUrl?.firstOrNull())
//            .placeholder(R.drawable.placeholder)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(binding.productImg)
//
//        binding.productName.text =
//            orderLine.article?.getDesignation(LocaleHelper.getLanguage(context))
//        binding.productDescription.text =
//            orderLine.article?.getDesignation(LocaleHelper.getLanguage(context))



        /*orderLine.discountAmountTtc?.let { _discount ->
            if (_discount > 0) {
                binding.productSavedPrice.visibility = View.VISIBLE
                binding.productSavedPrice.text = LocaleHelper.replaceArabicNumbers(
                    resources.getString(
                        R.string.saved_price,
                        orderLine.discountAmount
                    )
                )
            }
        }*/

        binding.productQte.text = orderLine.quantity.toString()
        articleQty = orderLine.quantity


        if (orderLine.isGift == true) {
            binding.productDeleteBtn.visibility = INVISIBLE
            binding.productQtePlus.visibility = INVISIBLE
            binding.productQteMinusBtn.visibility = INVISIBLE
            binding.productOrderViewParent.setBackgroundColor(
                ContextCompat.getColor(context, R.color.content_softGrey)
            )
        }
    }

    private fun updateProductQuantity() {
        binding.productQte.text = articleQty.toString()
    }

    data class ProductCartViewData(val data: OrderLine, val listener: ProductListener)
}
