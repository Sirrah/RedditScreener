package nl.sirrah.redditscreener.fragments

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.trello.rxlifecycle.components.support.RxFragment
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.common.extensions.inflate
import nl.sirrah.redditscreener.common.extensions.setImageUri
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find

class DetailFragment : RxFragment(), AnkoLogger {

    private var url: String? = null

    private var description: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = container!!.inflate(R.layout.fragment_detail)

        url = arguments?.get("url") as? String;
        description = arguments?.get("description") as? String;

        // alternatively use the image property after the view has been fully loaded, i.e. in #onActivityCreated
        (view.findViewById(R.id.image) as SimpleDraweeView).setImageUri(url)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setTitle(description)
    }

    /**
     * If there is a toolbar, set it's title to the given string
     */
    private fun setTitle(title: String?) {
        if (title != null) {
            val toolbar: Toolbar? = activity?.find(R.id.toolbar)
            toolbar?.title = title
        }
    }
}