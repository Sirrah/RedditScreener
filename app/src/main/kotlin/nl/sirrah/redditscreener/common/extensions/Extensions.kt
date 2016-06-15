package nl.sirrah.redditscreener.common.extensions

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.link_item.view.*
import org.jetbrains.anko.find

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun SimpleDraweeView.setImageUri(uri: String) {
    if (!TextUtils.isEmpty(uri)) {
        image.setImageURI(Uri.parse(uri))
    }
}

fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snackbar = Snackbar.make(this, text, duration)
    snackbar.init()
    snackbar.show()
    return snackbar
}

fun View.snackbar(text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snackbar = Snackbar.make(this, text, duration)
    snackbar.init()
    snackbar.show()
    return snackbar
}

fun Fragment.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        view!!.snackbar(text, duration, init)

fun Fragment.snackbar(text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        view!!.snackbar(text, duration, init)

fun Activity.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        find<View>(android.R.id.content).snackbar(text, duration, init)

fun Activity.snackbar(text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        find<View>(android.R.id.content).snackbar(text, duration, init)

fun isAPIVersionOrAbove(apiVersion: Int, func: () -> Unit ) {
    if (Build.VERSION.SDK_INT >- apiVersion) {
        func()
    }
}