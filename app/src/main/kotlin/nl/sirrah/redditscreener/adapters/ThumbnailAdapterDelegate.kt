package nl.sirrah.redditscreener.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.link_item.view.*
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.api.Link
import nl.sirrah.redditscreener.common.adapters.ViewType
import nl.sirrah.redditscreener.common.adapters.ViewTypeDelegateAdapter
import nl.sirrah.redditscreener.common.extensions.inflate

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
            val uri = android.net.Uri.parse(item.thumbnail)
            Picasso.with(context)
                    .load(uri)
                    .fit()
                    .centerCrop()
                    .error(nl.sirrah.redditscreener.R.drawable.ic_broken_image_black_48dp)
                    .placeholder(nl.sirrah.redditscreener.R.drawable.ic_landscape_black_48dp)
                    .into(image)

            image.setOnClickListener {
                // TODO replace the thumbnail with the larger image and rescale the grid item or
                // transition to a larger sized fragment or activity

                // TODO start another activity to display the larger image
//                val context = target.getContext()
//                val intent = Intent(context, )
//                intent.setDataAndType(uri, "image/*")
//
//                context.startActivity(intent)
            }
        }
    }
}
