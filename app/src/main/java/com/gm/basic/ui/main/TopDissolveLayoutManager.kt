package com.gm.basic.ui.main

import android.graphics.Rect
import android.util.ArrayMap
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-09.
 */
class TopDissolveLayoutManager : RecyclerView.LayoutManager() {
    private var scroll = 0
    private val locationRects: SparseArray<Rect> = SparseArray()
    private val attachedItems = SparseBooleanArray()
    private val viewTypeHeightMap: ArrayMap<Int, Int> = ArrayMap()
    private var needSnap = false
    private var lastDy = 0
    private var maxScroll = -1
    private var adapter: RecyclerView.Adapter<*>? = null
    private var recycler: Recycler? = null
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter)
        adapter = newAdapter
    }

    override fun onLayoutChildren(
        recycler: Recycler,
        state: RecyclerView.State
    ) {
        this.recycler = recycler
        if (state.isPreLayout) {
            return
        }
        buildLocationRects()
        detachAndScrapAttachedViews(recycler)
        layoutItemsOnCreate(recycler)
    }

    private fun buildLocationRects() {
        locationRects.clear()
        attachedItems.clear()
        var tempPosition = paddingTop
        val itemCount = itemCount
        for (i in 0 until itemCount) {
            val viewType = adapter!!.getItemViewType(i)
            var itemHeight: Int
            if (viewTypeHeightMap.containsKey(viewType)) {
                itemHeight = viewTypeHeightMap.get(viewType)!!
            } else {
                val itemView = recycler!!.getViewForPosition(i)
                addView(itemView)
                measureChildWithMargins(
                    itemView,
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
                )
                itemHeight = getDecoratedMeasuredHeight(itemView)
                viewTypeHeightMap.put(viewType, itemHeight)
            }

            val rect = Rect()
            rect.left = paddingLeft
            rect.top = tempPosition
            rect.right = width - paddingRight
            rect.bottom = rect.top + itemHeight
            locationRects.put(i, rect)
            attachedItems.put(i, false)
            tempPosition += itemHeight
        }
        if (itemCount == 0) {
            maxScroll = 0
        } else {
            computeMaxScroll()
        }
    }


    fun findFirstVisibleItemPosition(): Int {
        val count = locationRects.size()
        val displayRect = Rect(0, scroll, width, height + scroll)
        for (i in 0 until count) {
            if (Rect.intersects(displayRect, locationRects[i]) &&
                attachedItems[i]
            ) {
                return i
            }
        }
        return 0
    }

    private fun computeMaxScroll() {
        maxScroll = locationRects[locationRects.size() - 1].bottom - height
        if (maxScroll < 0) {
            maxScroll = 0
            return
        }
        val itemCount = itemCount
        var screenFilledHeight = 0
        for (i in itemCount - 1 downTo 0) {
            val rect: Rect = locationRects[i]
            screenFilledHeight += (rect.bottom - rect.top)
            if (screenFilledHeight > height) {
                val extraSnapHeight: Int =
                    height - (screenFilledHeight - (rect.bottom - rect.top))
                maxScroll += extraSnapHeight
                break
            }
        }
    }

    private fun layoutItemsOnCreate(recycler: Recycler) {
        val itemCount = itemCount
        val displayRect = Rect(0, scroll, width, height + scroll)
        for (i in 0 until itemCount) {
            val thisRect: Rect = locationRects[i]
            if (Rect.intersects(displayRect, thisRect)) {
                val childView = recycler.getViewForPosition(i)
                addView(childView)
                measureChildWithMargins(
                    childView,
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
                )
                layoutItem(childView, locationRects[i])
                attachedItems.put(i, true)
                childView.pivotY = 0F
                childView.pivotX = (childView.measuredWidth / 2).toFloat()
                if (thisRect.top - scroll > height) {
                    break
                }
            }
        }
    }

    private fun layoutItemsOnScroll() {
        val childCount = childCount
        val itemCount = itemCount
        val displayRect = Rect(0, scroll, width, height + scroll)
        var firstVisiblePosition = -1
        var lastVisiblePosition = -1
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i) ?: continue
            val position = getPosition(child)
            if (!Rect.intersects(displayRect, locationRects[position])) {
                removeAndRecycleView(child, recycler!!)
                attachedItems.put(position, false)
            } else {
                if (lastVisiblePosition < 0) {
                    lastVisiblePosition = position
                }
                firstVisiblePosition = if (firstVisiblePosition < 0) {
                    position
                } else {
                    Math.min(firstVisiblePosition, position)
                }
                layoutItem(child, locationRects[position])
            }
        }

        if (firstVisiblePosition > 0) {
            for (i in firstVisiblePosition - 1 downTo 0) {
                if (Rect.intersects(displayRect, locationRects[i]) &&
                    !attachedItems[i]
                ) {
                    reuseItemOnSroll(i, true)
                } else {
                    break
                }
            }
        }

        for (i in lastVisiblePosition + 1 until itemCount) {
            if (Rect.intersects(displayRect, locationRects[i]) &&
                !attachedItems[i]
            ) {
                reuseItemOnSroll(i, false)
            } else {
                break
            }
        }
    }


    private fun reuseItemOnSroll(position: Int, addViewFromTop: Boolean) {
        val scrap = recycler!!.getViewForPosition(position)
        measureChildWithMargins(scrap, 0, 0)
        scrap.pivotY = 0F
        scrap.pivotX = (scrap.measuredWidth / 2).toFloat()
        if (addViewFromTop) {
            addView(scrap, 0)
        } else {
            addView(scrap)
        }

        layoutItem(scrap, locationRects[position])
        attachedItems.put(position, true)
    }

    private fun layoutItem(child: View, rect: Rect) {
        val topDistance: Int = scroll - rect.top
        val layoutTop: Int
        val layoutBottom: Int
        val itemHeight: Int = rect.bottom - rect.top
        if (topDistance in 1 until itemHeight) {
            val rate1 = topDistance.toFloat() / itemHeight
            val rate2 = 1 - rate1 * rate1 / 3
            val rate3 = 1 - rate1 * rate1
            child.scaleX = rate2
            child.scaleY = rate2
            child.alpha = rate3
            layoutTop = 0
            layoutBottom = itemHeight
        } else {
            child.scaleX = 1F
            child.scaleY = 1F
            child.alpha = 1F
            layoutTop = rect.top - scroll
            layoutBottom = rect.bottom - scroll
        }
        layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom)
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: Recycler?,
        state: RecyclerView.State
    ): Int {
        if (itemCount == 0 || dy == 0) {
            return 0
        }
        var travel = dy
        if (dy + scroll < 0) {
            travel = -scroll
        } else if (dy + scroll > maxScroll) {
            travel = maxScroll - scroll
        }
        scroll += travel
        lastDy = dy
        if (!state.isPreLayout && childCount > 0) {
            layoutItemsOnScroll()
        }
        return travel
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        StartSnapHelper().attachToRecyclerView(view)
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            needSnap = true
        }
        super.onScrollStateChanged(state)
    }

    fun getSnapHeight(): Int {
        if (!needSnap) {
            return 0
        }
        needSnap = false
        val displayRect = Rect(0, scroll, width, height + scroll)
        val itemCount = itemCount
        for (i in 0 until itemCount) {
            val itemRect: Rect = locationRects[i]
            if (displayRect.intersect(itemRect)) {
                if (lastDy > 0) {
                    if (i < itemCount - 1) {
                        val nextRect: Rect = locationRects[i + 1]
                        return nextRect.top - displayRect.top
                    }
                }
                return itemRect.top - displayRect.top
            }
        }
        return 0
    }

    fun findSnapView(): View? {
        return if (childCount > 0) {
            getChildAt(0)
        } else null
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

//    init {
//        isAutoMeasureEnabled = true
//    }
}