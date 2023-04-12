package ghoudan.anfaSolution.com.common_ui.alert

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.common_ui.databinding.ChariSnackBarContentViewBinding
import com.google.android.material.snackbar.ContentViewCallback

class AnfaSnackBarContentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), ContentViewCallback {

    init {
        clipToPadding = false
    }

    private val binding = ChariSnackBarContentViewBinding.inflate(
        LayoutInflater.from(context), this
    )

    fun bind(uiModel: AnfaSnackBar.ChariSnackBarUiModel) {
        binding.root.apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    uiModel.backgroundColorResId
                )
            )
        }
        binding.contentRootView.apply {
            this.text = uiModel.title
            this.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    uiModel.backgroundColorResId
                )
            )
            this.setTextColor(
                ContextCompat.getColor(
                    context,
                    uiModel.textColorResId
                )
            )
            uiModel.iconResId?.let {
                val startIconDrawable = ContextCompat.getDrawable(
                    context, uiModel.iconResId
                )
                startIconDrawable?.setTint(
                    ContextCompat.getColor(context, uiModel.textColorResId)
                )
                this.setCompoundDrawablesWithIntrinsicBounds(
                    startIconDrawable, null, null, null
                )
            }

        }
    }

    override fun animateContentIn(delay: Int, duration: Int) {
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}
