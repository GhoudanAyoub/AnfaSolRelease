package ghoudan.anfaSolution.com.common_ui.textView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.CmdStateTextViewBinding
import ghoudan.anfaSolution.com.utils.LocaleHelper
import ghoudan.anfaSolution.com.utils.OrderStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderStateTextView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @Inject
    lateinit var localHelper: LocaleHelper

    private val binding = CmdStateTextViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setState(state: Int) {
        when (state) {
            OrderStatus.LIVRE.intValue -> {
                setSuccessState()
            }
            OrderStatus.ANNULE.intValue -> {
                setCancelState()
            }
            OrderStatus.CONFIRME.intValue -> {
                setConfirmState()
            }
            OrderStatus.RETOURNE.intValue -> {
                setReturnState()
            }
        }
    }

    fun setText(text: String) {
        binding.orderStatusValue.text = text
    }

    private fun setSuccessState() {
        binding.orderStatusValue.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.status_success
                )
            )
            if (localHelper.isRtl())
                setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_check_circle
                    ), null
                )
            else
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_check_circle
                    ), null, null, null
                )
        }
    }

    private fun setCancelState() {
        binding.orderStatusValue.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.status_error
                )
            )
            if (localHelper.isRtl())
                setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_cancel
                    ), null
                )
            else
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_cancel
                    ), null, null, null
                )
        }
    }

    private fun setConfirmState() {
        binding.orderStatusValue.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.brand_secondary
                )
            )
            if (localHelper.isRtl())
                setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_pending
                    ), null
                )
            else
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_pending
                    ), null, null, null
                )
        }
    }

    private fun setReturnState() {
        binding.orderStatusValue.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.brand_primary
                )
            )
            if (localHelper.isRtl())
                setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_assignment_return
                    ), null
                )
            else
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_assignment_return
                    ), null, null, null
                )
        }
    }

}
