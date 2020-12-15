package com.gsanguinetti.reddittopposts.base.presentation

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class DividerSpacingDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.set(0, 0, 0, offset)
    }
}