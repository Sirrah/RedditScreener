package nl.sirrah.redditscreener.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.link_item.view.*
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.api.Link
import nl.sirrah.redditscreener.common.adapters.ViewType
import nl.sirrah.redditscreener.common.adapters.ViewTypeDelegateAdapter
import nl.sirrah.redditscreener.common.extensions.inflate
import nl.sirrah.redditscreener.common.extensions.setImageUri

class ThumbnailAdapterDelegate : ViewTypeDelegateAdapter {

    // TODO should be possible to remove these two methods so we only need to define the actual view
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ThumbnailViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as ThumbnailViewHolder
        holder.bind(item as Link)
    }

    class ThumbnailViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.link_item)) {

        fun bind(item: Link) = with(itemView) {
            image.setImageUri(item.preview.images.first().source.url)
        }
    }
}
