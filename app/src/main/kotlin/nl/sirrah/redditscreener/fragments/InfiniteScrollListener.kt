package nl.sirrah.redditscreener.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import nl.sirrah.redditscreener.common.extensions.snackbar

class InfiniteScrollListener(val func: () -> Unit) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = 2
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        // FIXME not typesafe
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (dy > 0) {
            visibleItemCount = recyclerView.childCount;
            totalItemCount = layoutManager.itemCount;
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                recyclerView.snackbar("Getting more...")
                func()
                loading = true;
            }
        }
    }
}