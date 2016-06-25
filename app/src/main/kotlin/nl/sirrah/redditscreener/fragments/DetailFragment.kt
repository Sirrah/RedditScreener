package nl.sirrah.redditscreener.fragments

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.common.extensions.inflate
import nl.sirrah.redditscreener.common.extensions.snackbar
import nl.sirrah.redditscreener.common.views.ZoomableDraweeView
import org.jetbrains.anko.find

class DetailFragment : BaseFragment() {

    private var url: String? = null

    private var description: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = container!!.inflate(R.layout.fragment_detail)

        url = arguments?.get("url") as? String;
        description = arguments?.get("description") as? String;

        // alternatively use the image property after the view has been fully loaded, i.e. in #onActivityCreated
        val image: ZoomableDraweeView = view.find(R.id.image)

        //Listener for the fresco image loader to react on success/failure
        val controllerListener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                //Get the height of the actionBar for the current theme. This height is used to
                //determine the panning-height for the imageView.
                val tv = TypedValue()

                context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)
                val actionBarHeight = resources.getDimensionPixelSize(tv.resourceId)
                image.actionBarHeight = actionBarHeight
            }

            override fun onFailure(id: String?, e: Throwable?) {
                snackbar("Error while loading image: ${e?.message}")
            }
        }

        val controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(url)
                .build()

        image.setController(controller)
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