package tech.hombre.freelancehunt.common.widgets

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScroll : RecyclerView.OnScrollListener() {

    private var previousTotalItemCount = 0
    private var loading = true
    private val visibleThreshold = 5
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    private val startingPageIndex = 0
    private var currentPage = 0
    private var newlyAdded = true

    override fun onScrolled(mRecyclerView: RecyclerView, dx: Int, dy: Int) {
        val mLayoutManager: LinearLayoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        visibleItemCount = mRecyclerView.childCount
        totalItemCount = mLayoutManager.itemCount
        firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
        onScroll(firstVisibleItem, visibleItemCount, totalItemCount)
    }

    private fun onScroll(firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (newlyAdded) {
            newlyAdded = false
            return
        }
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount + 1
            if (totalItemCount == 0) {
                loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount + 1
            currentPage++
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem +
            visibleThreshold
        ) {
            onLoadMore(currentPage + 1, totalItemCount)
            loading = true
        }
    }


    abstract fun onLoadMore(page: Int, totalItemsCount: Int)
}