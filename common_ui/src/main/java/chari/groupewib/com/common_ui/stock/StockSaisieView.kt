package ghoudan.anfaSolution.com.common_ui.stock

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import chari.groupewib.com.common_ui.stock.StockViewListener
import chari.groupewib.com.networking.entity.StockSaisieEntity
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import ghoudan.anfaSolution.com.common_ui.databinding.ProductOrderViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class StockSaisieView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ProductOrderViewBinding.inflate(LayoutInflater.from(context), this)

    @ModelProp
    fun bind(stockSaisieViewState: StockSaisieViewState) {
        val (stock, stockViewListener) = stockSaisieViewState
        binding.stockSaisieCntr.visibility = VISIBLE
        binding.productName.text =
            stock.no.toString()
        binding.productDescription.text =
            stock.description
        binding.itemUnits.text =
            "Reste colis: ${stock.resteColis}"
        binding.itemWeight.text =
            "Reste Poids : ${stock.restePoids}"
        binding.colieAcht.text =
            "Colis achat : ${stock.colisAchatPL}"
        binding.colieRf.text =
            "Qté colis R+F : ${stock.qteColisRF}"
        binding.delete.isVisible = false

        binding.root.setOnClickListener {
            stockViewListener.onItemClicked(stock)
        }

    }
}

data class StockSaisieViewState(
    val stock: StockSaisieEntity,
    val stockViewListener: StockViewListener,
)
