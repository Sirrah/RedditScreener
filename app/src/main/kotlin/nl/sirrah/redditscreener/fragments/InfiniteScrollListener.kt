package nl.sirrah.redditscreener.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import nl.sirrah.redditscreener.common.extensions.snackbar

class InfiniteScrollListener(val onEndReached: () -> Unit) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = 2
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            visibleItemCount = recyclerView.childCount
            totalItemCount = recyclerView.layoutManager.itemCount

            // FIXME is this a decent fallback?
            firstVisibleItem = (recyclerView.layoutManager as? LinearLayoutManager)
                    ?.findFirstVisibleItemPosition() ?: 0

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                recyclerView.snackbar("Getting more...")
                onEndReached()
                loading = true
            }
        }
    }
}