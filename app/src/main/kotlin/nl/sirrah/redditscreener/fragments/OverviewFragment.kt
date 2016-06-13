package nl.sirrah.redditscreener.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.adapters.LinkAdapter
import nl.sirrah.redditscreener.api.RedditService
import nl.sirrah.redditscreener.common.extensions.inflate
import org.jetbrains.anko.find
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OverviewFragment : Fragment() {
    // TODO still necessary if we use Timber for logging?
    val TAG = javaClass.simpleName

    val redditService by lazy { RedditService.create() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        val view = container?.inflate(R.layout.fragment_overview)

        if (view != null) {
            val linksList: RecyclerView = view.find(R.id.links)
            linksList.layoutManager = GridLayoutManager(context, 2)

            redditService.listing("awww")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { listing ->
                        Log.d(TAG, "Received: $listing")
                        linksList.adapter = LinkAdapter(listing.children);
                    }
        }

        return view;
    }
}