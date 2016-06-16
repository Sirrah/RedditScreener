package nl.sirrah.redditscreener.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_overview.*
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.adapters.AdapterConstants
import nl.sirrah.redditscreener.adapters.DelegateAdapter
import nl.sirrah.redditscreener.adapters.ThumbnailAdapterDelegate
import nl.sirrah.redditscreener.api.Link
import nl.sirrah.redditscreener.api.RedditResponse
import nl.sirrah.redditscreener.api.Services
import nl.sirrah.redditscreener.common.extensions.inflate
import nl.sirrah.redditscreener.common.extensions.snackbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OverviewFragment : RxFragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container!!.inflate(R.layout.fragment_overview)
    }

    private val linkAdapter = DelegateAdapter<Link>()
            .addDelegate(AdapterConstants.LINK, ThumbnailAdapterDelegate())

    private var lastItem: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        links.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = linkAdapter

            addOnScrollListener(InfiniteScrollListener (onEndReached = {
                loadSubReddit("awww")
            }))
        }

        // TODO shouldn't it also work without loading any initial data due to the scroll listener?
        loadSubReddit("awww")
    }

    private fun loadSubReddit(subreddit: String) {
        Services.reddit.listing(subreddit, after = lastItem, limit = 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose (bindToLifecycle<RedditResponse>())
                .subscribe({ response ->
                    linkAdapter.addItems(response.data.children.map { it.data });
                    lastItem = response.data.after

                    setTitle("/r/$subreddit")
                }, { e ->
                    snackbar("Caught exception: ${e.message}", Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry) {
                                // Reset the list to the beginning
                                lastItem = ""
                                loadSubReddit(subreddit)
                            }
                })
    }

    /**
     * If there is a toolbar, set it's title to the given string
     */
    private fun setTitle(title: String) {
        val toolbar: Toolbar? = activity?.find(R.id.toolbar)
        toolbar?.title = title
    }
}