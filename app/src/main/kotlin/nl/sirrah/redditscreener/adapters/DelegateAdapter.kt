package nl.sirrah.redditscreener.adapters

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import nl.sirrah.redditscreener.common.adapters.ViewType
import nl.sirrah.redditscreener.common.adapters.ViewTypeDelegateAdapter
import java.util.*

open class DelegateAdapter<T : ViewType> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val delegates = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val items: ArrayList<ViewType>

    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegates.put(AdapterConstants.LOADING, LoadingAdapterDelegate())
        items = ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getViewType()
    }

    fun addItems(newItems: List<T>) {
        // TODO only remove the last item if it is the loading indicator
        val initPosition = items.size - 1
        items.removeAt(initPosition)

        // TODO why two separate notifications?
        notifyItemRemoved(initPosition)

        items.addAll(newItems)
        items.add(loadingItem)

        // FIXME is this correct, shouldn't this be links.size + 1?
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun reset(newItems: List<T>) {
        items.clear()
        // TODO why two separate notifications?
        // FIXME this seems broken, getLastPosition will always return 0
        notifyItemRangeRemoved(0, getLastPosition())


        items.addAll(newItems)
        items.add(loadingItem)
        notifyItemRangeInserted(0, items.size)
    }

    @Suppress("UNCHECKED_CAST")
    fun getItems(): List<T> {
        return items
                .filter { it.getViewType() != AdapterConstants.LOADING }
                .map { it as T }
    }

    fun addDelegate(viewType: Int, delegate: ViewTypeDelegateAdapter) : DelegateAdapter<T> {
        delegates.put(viewType, delegate)
        return this
    }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex
}