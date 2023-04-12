package ghoudan.anfaSolution.com.common_ui.buttons

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ChariSwitchBtnBinding
import ghoudan.anfaSolution.com.utils.AnfaLocal

class AnfaSwitchBtn : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var callback: ((Int) -> Unit)? = null


    private val binding = ChariSwitchBtnBinding.inflate(LayoutInflater.from(context), this)

    init {
        setupView()
        layoutDirection = LAYOUT_DIRECTION_LTR
        binding.leftRadioBtnText.text =
            String.format(AnfaLocal.ENGLISH, resources.getString(R.string.switch_to_french))
        binding.rightRadioBtn.text =
            String.format(AnfaLocal.ARABIC_MOROCCO, resources.getString(R.string.switch_to_arabic))
    }

    private fun setupView() {

        handleSwitchState()

        binding.leftRadioBtnContainer.setOnClickListener {
            if (!binding.leftRadioBtn.isChecked) {
                binding.leftRadioBtn.isChecked = true
                binding.rightRadioBtn.isChecked = false
                callback?.invoke(-1)
            }
        }

        binding.rightRadioBtnContainer.setOnClickListener {
            if (!binding.rightRadioBtn.isChecked) {
                binding.rightRadioBtn.isChecked = true
                binding.leftRadioBtn.isChecked = false
                callback?.invoke(1)
            }

        }
    }

    fun bindView(data: ChariRadioButtonSwitchData, callback: (Int) -> Unit) {
        this.callback = callback
        binding.leftRadioBtnText.text = data.leftText
        binding.rightRadioBtnText.text = data.rightText
    }


    private fun handleSwitchState() {
        val checkedDrawable =
            ContextCompat.getDrawable(context, R.drawable.bg_rounded_primary)
        val uncheckedDrawable =
            ContextCompat.getDrawable(context, R.drawable.bg_rounded_pastel)

        if (binding.leftRadioBtn.isChecked) {
            binding.leftRadioBtnContainer.background = checkedDrawable
            binding.leftRadioBtnText.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.content_white
                )
            )
            binding.rightRadioBtnText.setTextColor(
                ContextCompat.getColor
                    (context, R.color.brand_hover)
            )
            binding.rightRadioBtnContainer.background = uncheckedDrawable

        } else {
            binding.leftRadioBtnContainer.background = uncheckedDrawable
            binding.leftRadioBtnText.setTextColor(
                ContextCompat.getColor(
                    context, R.color.brand_hover
                )
            )
            binding.rightRadioBtnText.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.content_white
                )
            )
            binding.rightRadioBtnContainer.background = checkedDrawable
        }
    }

    fun setSwitchInitialState(selectedLanguageId: Int) {

        if (selectedLanguageId == -1) {
            //French
            binding.leftRadioBtnContainer.background = ContextCompat.getDrawable(
                context, R.drawable.bg_rounded_primary
            )
            binding.leftRadioBtnText.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.content_white
                )
            )

            binding.rightRadioBtnContainer.background = ContextCompat.getDrawable(
                context, R.drawable.bg_rounded_pastel
            )
            binding.rightRadioBtnText.setTextColor(
                ContextCompat.getColor(
                    context, R.color.brand_hover
                )
            )
        } else if (selectedLanguageId == 1) {
            //Arabic
            binding.rightRadioBtnContainer.background = ContextCompat.getDrawable(
                context, R.drawable.bg_rounded_primary
            )
            binding.rightRadioBtnText.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.content_white
                )
            )

            binding.leftRadioBtnContainer.background = ContextCompat.getDrawable(
                context, R.drawable.bg_rounded_pastel
            )
            binding.leftRadioBtnText.setTextColor(
                ContextCompat.getColor(
                    context, R.color.brand_hover
                )
            )
        }
    }


    data class ChariRadioButtonSwitchData(
        val leftText: String,
        val rightText: String
    )
}
