package ghoudan.anfaSolution.com.common_ui.inputFields

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText

open class ChariInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    enum class State {
        IDLE,
        ERROR,
        SUCCESS,
        HOVER,
        FULL
    }
}

fun ChariInputField.hideKeyboard(inputEditText: AppCompatEditText) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
}

fun ChariInputField.showKeyboard(inputEditText: AppCompatEditText) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(inputEditText, InputMethodManager.SHOW_FORCED)
}
