package nl.sirrah.redditscreener.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.adapters.LinkAdapter
import nl.sirrah.redditscreener.api.RedditService
import org.jetbrains.anko.find
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    val TAG = javaClass.simpleName

    lateinit var redditService: RedditService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linksList: RecyclerView = find(R.id.links)
        linksList.layoutManager = GridLayoutManager(this, 3)

        redditService = RedditService.create()

        redditService.listing("awww")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listing ->
                    Log.d(TAG, "Received: $listing")
                    linksList.adapter = LinkAdapter(listing.children);
                }
    }
}
