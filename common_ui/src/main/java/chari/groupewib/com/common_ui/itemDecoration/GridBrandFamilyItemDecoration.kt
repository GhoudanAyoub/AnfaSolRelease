package ghoudan.anfaSolution.com.common_ui.itemDecoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class GridBrandFamilyItemDecoration(private val spanCount: Int, @Px private val spacing: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val column = position % spanCount

        val layoutDirection = parent.context.resources.configuration.layoutDirection
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            when (column) {
                0 -> {
                    //start
                    outRect.left = spacing / 2
                }
                spanCount - 1 -> {
                    //end
                    outRect.right = spacing / 2
                }
                else -> {
                    //middle
                    outRect.right = spacing / 2
                    outRect.left = spacing / 2
                }
            }
        } else {
            when (column) {
                0 -> {
                    //start
                    outRect.right = spacing / 2
                }
                spanCount - 1 -> {
                    //end
                    outRect.left = spacing / 2
                }
                else -> {
                    //middle
                    outRect.left = spacing / 2
                    outRect.right = spacing / 2
                }
            }
        }


        if (position < spanCount) {
            outRect.top = spacing
        }
        outRect.bottom = spacing
    }
}
