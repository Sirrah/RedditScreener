package nl.sirrah.redditscreener.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.api.Link
import org.jetbrains.anko.find

class LinkAdapter(val data: List<Link>) : RecyclerView.Adapter<LinkAdapter.ViewHolder>() {

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val itemView = inflater.inflate(R.layout.link_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(data[position]) {
            val target: ImageView = holder.rootView.find(R.id.image)

            val uri = Uri.parse(thumbnail)
            Picasso.with(holder.rootView.context)
                    .load(uri)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.ic_broken_image_black_48dp)
                    .placeholder(R.drawable.ic_landscape_black_48dp)
                    .into(target)

            target.setOnClickListener {
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

    class ViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)
}