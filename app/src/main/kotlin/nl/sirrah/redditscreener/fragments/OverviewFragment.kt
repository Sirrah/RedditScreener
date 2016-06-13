package nl.sirrah.redditscreener.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.adapters.LinkAdapter
import nl.sirrah.redditscreener.api.Listing
import nl.sirrah.redditscreener.api.RedditService
import nl.sirrah.redditscreener.common.extensions.inflate
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.find
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OverviewFragment : RxFragment(), AnkoLogger {
    val redditService by lazy { RedditService.create() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        val view = container?.inflate(R.layout.fragment_overview)

        if (view != null) {
            val linksList: RecyclerView = view.find(R.id.links)
            linksList.layoutManager = GridLayoutManager(context, 2)

            val subreddit = "awww"
            redditService.listing(subreddit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose (bindToLifecycle<Listing>())
                    .subscribe { listing ->
                        debug("Received: $listing")
                        linksList.adapter = LinkAdapter(listing.children);

                        setTitle("/r/" + subreddit)
                    }
        }

        return view;
    }

    fun setTitle(title: String) {
        val toolbar: Toolbar? = activity?.find(R.id.toolbar)
        toolbar?.title = title
    }
}