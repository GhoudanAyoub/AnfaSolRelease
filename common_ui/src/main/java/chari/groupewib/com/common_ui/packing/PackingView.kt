package ghoudan.anfaSolution.com.common_ui.stock

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import chari.groupewib.com.common_ui.packing.PackingViewListener
import chari.groupewib.com.networking.entity.PackingListEntity
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ProductOrderViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PackingView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ProductOrderViewBinding.inflate(LayoutInflater.from(context), this)

    @ModelProp
    fun bind(
        packingViewModel: PackingViewModel,
    ) {
        val (stock, listener) = packingViewModel
        var isChecked = stock.isChecked

        if (isChecked) {
            binding.checkBox.setImageResource(R.drawable.baseline_check_box_24)
        } else {
            binding.checkBox.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
        }
        
        binding.stockSaisieCntr.visibility = VISIBLE
        binding.productName.text =
            stock.codeFour.toString()
        binding.productDescription.text =
            stock.dateAch
        binding.itemUnits.text =
            "Reste colis: ${stock.resteColis}"
        binding.itemWeight.text =
            "Reste Poids : ${stock.restePoids}"
        binding.colieAcht.text =
            "Saisie Colis : ${stock.saisieColis}"
        binding.colieRf.text =
            "Num  colis : ${stock.numColis}"
        binding.delete.isVisible = false
        binding.checkBox.isVisible = true

        binding.checkBox.setOnClickListener {
            if (isChecked) {
                isChecked = false
                binding.checkBox.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
            } else {
                isChecked = true
                binding.checkBox.setImageResource(R.drawable.baseline_check_box_24)
            }
            stock.isChecked = isChecked
            listener.onItemClicked(stock,stock.restePoids.toDouble(), isChecked)
        }

    }
}

data class PackingViewModel(
    val stock: PackingListEntity,
    val listener: PackingViewListener,
)
