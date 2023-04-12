package ghoudan.anfaSolution.com.common_ui.inputFields

import android.content.Context
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ChariTextFieldBinding

class AnfaTextField(context: Context, attrs: AttributeSet? = null) :
        ChariInputField(context, attrs) {

    private val binding = ChariTextFieldBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    enum class RightIconAction {
        CLEAR_TEXT,
        HIDE_TEXT
    }

    init {
        setIdleState()
    }

    fun isEnabled(isEnabled: Boolean) {
        binding.inputEditText.isEnabled = isEnabled
    }

    fun setLeftIcon(resId: Int, @ColorRes tintResId: Int? = null) {
        binding.inputTextLeftIcon.setImageResource(resId)
        binding.inputTextLeftIcon.visibility = VISIBLE
        binding.inputPrefixContainer.visibility = VISIBLE
    }

    fun askFocus() {
        binding.inputEditText.requestFocus()
        binding.inputEditText.setSelection(binding.inputEditText.selectionEnd)
        showKeyboard(binding.inputEditText)
    }

    fun cleanFocus() {
        binding.inputEditText.clearFocus()
        hideKeyboard(binding.inputEditText)
    }

    fun setFocusChangeListener(listener: OnFocusChangeListener) {
        binding.inputEditText.onFocusChangeListener = listener
    }

    fun setInputType(inputType: Int) {
        binding.inputEditText.inputType = inputType
    }

    fun setImOption(imOptions: Int) {
        binding.inputEditText.imeOptions = imOptions
    }

    fun setEms(ems: Int) {
        binding.inputEditText.setEms(ems)
    }

    fun setHint(@StringRes hintResId: Int) {
        binding.inputEditText.setHint(hintResId)
    }

    fun setHintColor(@ColorInt tintResId: Int) {
        binding.inputEditText.setHintTextColor(tintResId)
    }

    fun setText(text: String?) {
        binding.inputEditText.setText(text, TextView.BufferType.EDITABLE)
    }

    fun setText(@StringRes resId: Int) {
        binding.inputEditText.setText(resources.getString(resId))
    }

    fun getText(): String {
        return binding.inputEditText.text.toString()
    }

    fun setInputTextSingleLine() {
        binding.inputEditText.isSingleLine = true
    }

    fun setState(state: State, errorMessage: String? = null) {
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

    fun addOnTextChangedListener(textWatcher: TextWatcher) {
        binding.inputEditText.addTextChangedListener(textWatcher)
    }

    fun addKeyEventListener(keyEventListener: OnKeyListener) {
        binding.inputEditText.setOnKeyListener(keyEventListener)
    }

    fun setOnEditorActionListener(editorActionListener: TextView.OnEditorActionListener) {
        binding.inputEditText.setOnEditorActionListener(editorActionListener)
    }

    fun setOnEditorTouchListener(editorClickListener: OnTouchListener) =
        binding.inputEditText.setOnTouchListener(editorClickListener)

    fun setMaxLength(maxLength: Int) {
        binding.inputEditText.filters += InputFilter.LengthFilter(maxLength)
    }

    fun setMaxLines(maxLines: Int) {
        binding.inputEditText.maxLines = maxLines
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
                    R.color.content_dark
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
                    R.color.content_dark
                )
            )
        }
        setTint(R.drawable.bg_text_field_success)
    }

    private fun setHoverState() {
        binding.inputEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.content_dark
                )
            )
        }
        setTint(R.drawable.bg_text_field_hover)
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

    private fun setTint(@DrawableRes tintResId: Int) {
        binding.inputContainer.background = ContextCompat.getDrawable(context, tintResId)
    }

    fun setInputGravity(gravity: Int) {
        binding.inputEditText.gravity = gravity

    }

    fun setInputTextAlignment(textAlignment: Int) {
        binding.inputEditText.textAlignment = textAlignment
    }

    fun setRtlDirection() {
        binding.inputContainer.layoutDirection = LAYOUT_DIRECTION_RTL
    }

    fun setTextAlignmentStart() {
        binding.inputEditText.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
    }

    fun setTextIcon(resId: Int) {
        binding.inputEditText.apply {
            setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                resId,
                0
            )
            compoundDrawablePadding =
                resources.getDimensionPixelOffset(R.dimen.spacing_xxl_7)
        }
    }

    fun setRightIcon(resId: Int, action: RightIconAction, callback: (() -> Unit)? = null) {
        binding.inputTextRighIcon.apply {
            setImageResource(resId)
            visibility = VISIBLE
            setOnClickListener {
                when (action) {
                    RightIconAction.HIDE_TEXT -> {
                        callback?.invoke()
                    }
                    RightIconAction.CLEAR_TEXT -> {
                        binding.inputEditText.text?.clear()
                        visibility = View.GONE
                    }
                }
            }
        }
    }

    fun hideRightIcon() {
        binding.inputTextRighIcon.visibility = GONE
    }

    fun clear() {
        binding.inputEditText.text?.clear()
    }
}
