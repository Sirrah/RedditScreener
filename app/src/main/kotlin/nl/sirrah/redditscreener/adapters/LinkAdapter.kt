package nl.sirrah.redditscreener.adapters

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import nl.sirrah.redditscreener.api.Link
import nl.sirrah.redditscreener.common.adapters.ViewType
import nl.sirrah.redditscreener.common.adapters.ViewTypeDelegateAdapter
import java.util.*

// TODO extract a generic version
class LinkAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val delegates = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val items: ArrayList<ViewType>

    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegates.put(AdapterConstants.LINK, ThumbnailAdapterDelegate())
        delegates.put(AdapterConstants.LOADING, LoadingAdapterDelegate())
        items = ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return items.get(position).getViewType()
    }

    fun addLinks(links: List<Link>) {
        // TODO only remove the last item if it is the loading indicator
        val initPosition = items.size - 1
        items.removeAt(initPosition)

        // TODO why two separate notifications?
        notifyItemRemoved(initPosition)

        items.addAll(links)
        items.add(loadingItem)

        // FIXME is this correct, shouldn't this be links.size + 1?
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun resetLinks(links: List<Link>) {
        items.clear()
        // TODO why two separate notifications?
        // FIXME this seems broken, getLastPosition will always return 0
        notifyItemRangeRemoved(0, getLastPosition())


        items.addAll(links)
        items.add(loadingItem)
        notifyItemRangeInserted(0, items.size)
    }

    fun getLinks(): List<Link> {
        return items
                .filter { it.getViewType() == AdapterConstants.LINK }
                .map { it as Link }
    }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex
}