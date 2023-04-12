package ghoudan.anfaSolution.com.common_ui.product

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ProductViewBinding
import ghoudan.anfaSolution.com.common_ui.product.data.ProductViewData
import ghoudan.anfaSolution.com.networking.di.CurrentUserProvider
import ghoudan.anfaSolution.com.utils.LocaleHelper
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject
import kotlin.math.roundToInt

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ProductView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ProductViewBinding.inflate(LayoutInflater.from(context), this)
    private val intendedWidth = resources.getDimension(R.dimen.product_view_item_width).toInt()
    private var state = QtyViewState.STATE_COLLAPSED
    private var articleQty = 0
    private var productListener: ProductListener? = null

    @Inject
    lateinit var userProvider: CurrentUserProvider

    @ModelProp
    fun bind(data: ProductViewData) {

        val ( listener, helper) = data
        productListener = listener
//
//        Glide.with(context)
//            .load(article.imageListUrl?.firstOrNull())
//            .placeholder(R.drawable.img_placeholder)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(binding.productImg)
//
//        binding.productTitle.text = article.getDesignation(LocaleHelper.getLanguage(context))


//        if (helper?.showQuantity == true) {
//            binding.qtyLayout.visibility = VISIBLE
//            binding.productQty.text = article.stockQuantity.toString()
//        } else {
//            binding.qtyLayout.visibility = GONE
//        }


        binding.productImg.setOnClickListener {
//            listener.onProductClicked(article, helper?.rankOnViewHolder)
        }

    }


    private fun expandQtyView() {
        state = QtyViewState.STATE_EXPANDED
        binding.qtyView.background =
            ContextCompat.getDrawable(context, R.drawable.bg_layout_add_elevator)
        binding.qtyDetails.visibility = VISIBLE
        binding.addQtyBtn.background =
            ContextCompat.getDrawable(context, R.drawable.bg_btn_qty_elevator)
        binding.removeQtyBtn.background =
            ContextCompat.getDrawable(context, R.drawable.bg_btn_qty_elevator)
    }

    private fun collapseQtyView() {
        articleQty = 0
        state = QtyViewState.STATE_COLLAPSED
        binding.qtyDetails.visibility = GONE
        binding.qtyView.background = null
        binding.addQtyBtn.background =
            ContextCompat.getDrawable(context, R.drawable.bg_btn_qty_bordered_elevator)
        binding.removeQtyBtn.background =
            ContextCompat.getDrawable(context, R.drawable.bg_btn_qty_bordered_elevator)
    }

    private fun updateProductQuantity() {
        binding.qtyArticle.text = articleQty.toString()
    }

    enum class QtyViewState {
        STATE_COLLAPSED,
        STATE_EXPANDED
    }
}
