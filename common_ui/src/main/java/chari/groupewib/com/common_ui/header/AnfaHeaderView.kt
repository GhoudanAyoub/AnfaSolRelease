package ghoudan.anfaSolution.com.common_ui.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.CollectionHeaderSearchViewBinding
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AnfaHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding =
        CollectionHeaderSearchViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.btnText.text = resources.getString(
            R.string.hint_search
        )
    }

    @ModelProp
    fun bind(data: ChariHeaderViewData) {
        setOnClickListener {
            data.listener.onSearchBtnClicked()
        }
    }

    data class ChariHeaderViewData(
        val listener: HeaderViewListener
    )
}
