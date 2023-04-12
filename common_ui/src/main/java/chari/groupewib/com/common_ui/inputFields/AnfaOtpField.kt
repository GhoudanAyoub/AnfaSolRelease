package ghoudan.anfaSolution.com.common_ui.inputFields

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ChariOtpFieldBinding

class AnfaOtpField(context: Context, attrs: AttributeSet? = null) :
    ChariInputField(context, attrs), AnfaAppCompatEditText.OnCutCopyPasteListener {


    enum class OtpFieldActions {
        PASTE
    }

    private val binding = ChariOtpFieldBinding.inflate(LayoutInflater.from(context), this)

    private var callback: ((otpActions: OtpFieldActions) -> Unit)? = null


    override fun onCut() {}

    override fun onCopy() {}

    override fun onPaste() {
        callback?.invoke(OtpFieldActions.PASTE)
    }

    init {
        setIdleState()
        binding.inputEditText.setCutCopyPastListener(this)
    }

    fun setActionsCallbackActions(callback: (otpActions: OtpFieldActions) -> Unit) {
        this.callback = callback
    }

    fun getText(): String {
        return binding.inputEditText.text.toString()
    }

    fun setText(text: String) {
        binding.inputEditText.setText(text)
        binding.inputEditText.setSelection(text.length)
    }

    fun askFocus() {
        binding.inputEditText.requestFocus()
        showKeyboard(binding.inputEditText)
    }

    override fun clearFocus() {
        super.clearFocus()
        hideKeyboard(binding.inputEditText)
    }

    fun getInput(): AppCompatEditText {
        return binding.inputEditText
    }

    fun clear() {
        binding.inputEditText.text?.clear()
    }

    fun setState(state: State) {
        when (state) {
            State.IDLE -> {
                setIdleState()
            }
            State.ERROR -> {
                setErrorState()
            }
            State.SUCCESS -> {
                setSuccessState()
            }
            State.HOVER -> {
                setHoverState()
            }
            State.FULL -> {
                setFullState()
            }
        }
    }

    fun isEnabled(isEnabled: Boolean) {
        binding.inputEditText.isEnabled = isEnabled
    }

    private fun setTint(@DrawableRes tintResId: Int) {
        binding.inputContainer.background = ContextCompat.getDrawable(context, tintResId)
    }

    fun setImOption(imOptions: Int) {
        binding.inputEditText.imeOptions = imOptions
    }

    fun addOnTextChangedListener(textWatcher: TextWatcher) {
        binding.inputEditText.addTextChangedListener(textWatcher)
    }

    fun addKeyEventListener(keyEventListener: OnKeyListener) {
        binding.inputEditText.setOnKeyListener(keyEventListener)
    }

    fun setOnEditorActionListener(editorActionListener: TextView.OnEditorActionListener) {
        binding.inputEditText.setOnEditorActionListener(editorActionListener)
    }

    private fun setErrorState() {
        binding.inputEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.status_error
                )
            )
        }
        setTint(R.drawable.bg_text_field_error)
    }

    private fun setIdleState() {
        binding.inputEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.content_lightGrey
                )
            )
        }
        setTint(R.drawable.bg_text_field_idle)
    }

    private fun setSuccessState() {
        binding.inputEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.status_success
                )
            )
        }
        setTint(R.drawable.bg_text_field_success)
    }

    private fun setFullState() {
        binding.inputEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.content_dark
                )
            )
        }
        setTint(R.drawable.bg_text_field_full)
    }

    private fun setHoverState() {
        binding.inputEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.status_alert
                )
            )
        }
        setTint(R.drawable.bg_text_field_hover)
    }
}
