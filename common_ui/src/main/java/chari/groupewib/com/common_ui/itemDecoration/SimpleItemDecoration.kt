package ghoudan.anfaSolution.com.common_ui.itemDecoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

/**
 * Adds [marginBetweenItemsPx] between each item.
 *
 * @param includeEdges Whether to include the margin at the start of the first item and the end of
 * the last item
 */
class SimpleItemDecoration(
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

        val layoutDirection = parent.context.resources.configuration.layoutDirection

        if (includeEdges) {
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                outRect.right = if (itemPosition == 0) {
                    marginBetweenItemsPx
                } else {
                    marginBetweenItemsPx
                }

                if (itemPosition == parent.adapter?.itemCount?.minus(1)) {
                    outRect.left = marginBetweenItemsPx
                }
            } else {
                outRect.left = if (itemPosition == 0) {
                    marginBetweenItemsPx
                } else {
                    marginBetweenItemsPx
                }

                if (itemPosition == parent.adapter?.itemCount?.minus(1)) {
                    outRect.right = marginBetweenItemsPx
                }
            }

        } else {
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                outRect.right = if (itemPosition == 0) {
                    0
                } else {
                    marginBetweenItemsPx
                }

            } else {
                outRect.left = if (itemPosition == 0) {
                    0
                } else {
                    marginBetweenItemsPx
                }
            }

        }

        outRect.top = marginBetweenItemsPx
        outRect.bottom = marginBetweenItemsPx
    }
}
