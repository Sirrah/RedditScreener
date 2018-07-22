package nl.sirrah.redditscreener.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_overview.*
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.R.id.links
import nl.sirrah.redditscreener.adapters.AdapterConstants
import nl.sirrah.redditscreener.adapters.DelegateAdapter
import nl.sirrah.redditscreener.adapters.ThumbnailAdapterDelegate
import nl.sirrah.redditscreener.api.Link
import nl.sirrah.redditscreener.api.Listing
import nl.sirrah.redditscreener.api.Services
import nl.sirrah.redditscreener.common.extensions.inflate
import nl.sirrah.redditscreener.common.extensions.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.find
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OverviewFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container!!.inflate(R.layout.fragment_overview)
    }

    private val linkAdapter = DelegateAdapter<Link>()
        .addDelegate(AdapterConstants.LINK, ThumbnailAdapterDelegate({ item ->
            // TODO what happened to Anko's #withArguments?
            val fragment = DetailFragment().apply {
                arguments = Bundle().apply {
                    val url = item.preview?.images?.first()?.source?.url
                    putString("url", url)
                    putString("description", item.title)
                }
            }
            activity.changeFragment(fragment)
        }))

    private var lastItem: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        links.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = linkAdapter

            addOnScrollListener(InfiniteScrollListener(onEndReached = {
                loadSubReddit("awww")
            }))
        }

        // TODO shouldn't it also work without loading any initial data due to the scroll listener?
        loadSubReddit("awww")
    }

    private fun loadSubReddit(subreddit: String) {
        // TODO use the RealmResults directly in the Adapter
        val result = realm.where(Link::class.java)
            .findAll()
        linkAdapter.addItems(result)

        Services.reddit.listing(subreddit, after = lastItem, limit = 20)
            .doOnNext { listing ->
                // Store in the database while still on the IO thread
                val realm = Realm.getDefaultInstance()
                realm.use {
                    realm.executeTransaction {
                        realm.copyToRealmOrUpdate(listing.children)
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle<Listing>())
            .subscribe({ listing ->
                linkAdapter.addItems(result)
                lastItem = listing.after

                setTitle("/r/$subreddit")
            }, { e ->
                error("Exception while retrieving subreddit", e)
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
        val toolbar: Toolbar? = activity.find(R.id.toolbar)
        toolbar?.title = title
    }
}