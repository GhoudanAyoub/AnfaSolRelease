package ghoudan.anfaSolution.com.common_ui.itemDecoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class SimpleVerticalItemDecoration(
    @Px val marginBetweenItemsPx: Int,
    val includeEdges: Boolean

) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition > 1) {
            outRect.right = marginBetweenItemsPx
            outRect.left = marginBetweenItemsPx
            outRect.top = marginBetweenItemsPx
        } else {
            outRect.right = 0
            outRect.left = 0
            outRect.top = 0
        }
        if (itemPosition == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = marginBetweenItemsPx
        }
    }
}
