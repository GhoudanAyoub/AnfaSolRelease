package ghoudan.anfaSolution.com.common_ui.inputFields

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class AnfaAppCompatEditText(context: Context, attrs: AttributeSet?) :
    AppCompatEditText(context, attrs) {

    interface OnCutCopyPasteListener {
        fun onCut()
        fun onCopy()
        fun onPaste()
    }

    private var cutCopyPasteListener: OnCutCopyPasteListener? = null

    override fun onTextContextMenuItem(id: Int): Boolean {
        val consumed = super.onTextContextMenuItem(id)

        when (id) {
            android.R.id.cut -> cutCopyPasteListener?.onCut()
            android.R.id.copy -> cutCopyPasteListener?.onCopy()
            android.R.id.paste -> cutCopyPasteListener?.onPaste()
        }
        return consumed
    }

    fun setCutCopyPastListener(listener: OnCutCopyPasteListener) {
        cutCopyPasteListener = listener
    }
}
