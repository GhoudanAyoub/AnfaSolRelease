package ghoudan.anfaSolution.com.common_ui.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ghoudan.anfaSolution.com.common_ui.databinding.CollectionSeparatorViewBinding
import com.airbnb.epoxy.ModelView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AnfaCollectionSeparatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding =
        CollectionSeparatorViewBinding.inflate(LayoutInflater.from(context), this)
}
