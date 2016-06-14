package nl.sirrah.redditscreener.common.extensions

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.link_item.view.*

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun SimpleDraweeView.setImageUri(uri: String) {
    if (!TextUtils.isEmpty(uri)) {
        image.setImageURI(Uri.parse(uri))
    }
}
