package nl.sirrah.redditscreener.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.common.adapters.ViewType
import nl.sirrah.redditscreener.common.adapters.ViewTypeDelegateAdapter
import nl.sirrah.redditscreener.common.extensions.inflate

class LoadingAdapterDelegate : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.loading))
}