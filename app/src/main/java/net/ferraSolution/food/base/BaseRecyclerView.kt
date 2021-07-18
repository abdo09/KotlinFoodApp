package net.ferraSolution.food.base

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView


class BaseRecyclerView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : EpoxyRecyclerView(context, attrs, defStyleAttr) {

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //adapter = null
        // Or use
        swapAdapter(null, true) //so that the existing views are recycled to the view pool
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        forceChildrenRecycler(layout as LinearLayoutManager)
    }

    private fun forceChildrenRecycler(linearLayoutManager: LinearLayoutManager) {
        linearLayoutManager.recycleChildrenOnDetach = true
    }
}

