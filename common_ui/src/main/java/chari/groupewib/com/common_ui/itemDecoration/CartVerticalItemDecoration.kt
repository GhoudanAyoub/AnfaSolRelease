package ghoudan.anfaSolution.com.common_ui.itemDecoration


import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class CartVerticalItemDecoration(
    @Px val marginBetweenItemsPx: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = marginBetweenItemsPx
        outRect.bottom = marginBetweenItemsPx
    }
}
