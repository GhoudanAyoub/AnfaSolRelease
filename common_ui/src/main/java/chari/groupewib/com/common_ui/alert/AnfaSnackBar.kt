package ghoudan.anfaSolution.com.common_ui.alert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import ghoudan.anfaSolution.com.common_ui.R
import ghoudan.anfaSolution.com.common_ui.databinding.ChariSnackBarBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class AnfaSnackBar(parent: ViewGroup, content: AnfaSnackBarContentView) :
        BaseTransientBottomBar<AnfaSnackBar>(parent, content, content) {

    init {
        getView().setPadding(0, 0, 0, 2)
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context, R.color.transparent
            )
        )
        getView().stateListAnimator = null
    }

    companion object {
        fun make(
            viewGroup: ViewGroup,
            anchorView: View? = null,
            uiModel: ChariSnackBarUiModel
        ): AnfaSnackBar {
            val chariSnackBarContentViewBinding = ChariSnackBarBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
            chariSnackBarContentViewBinding.root.bind(uiModel)
            return AnfaSnackBar(
                viewGroup,
                chariSnackBarContentViewBinding.root
            ).setDuration(Snackbar.LENGTH_SHORT).setAnchorView(anchorView)
        }
    }

    data class ChariSnackBarUiModel(
        val title: String,
        @ColorRes val backgroundColorResId: Int,
        @ColorRes val textColorResId: Int,
        @DrawableRes val iconResId: Int? = null
    )
}
