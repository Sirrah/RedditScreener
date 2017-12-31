package nl.sirrah.redditscreener.api

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import nl.sirrah.redditscreener.adapters.AdapterConstants
import nl.sirrah.redditscreener.common.adapters.ViewType

data class Listing(var children: List<Link>, var after: String, var before: String?)

/**
 * t3 (link) type
 */
open class Link : RealmObject(), ViewType {
    open var preview: Preview? = null
    open var domain: String? = null
    open var subreddit: String? = null
    open var selfttext: String? = null

    @PrimaryKey
    open var id: String? = null
    open var gilded: Int = 0
    open var archived: Boolean = false
    open var clicked: Boolean = false
    open var author: String? = null
    open var numComments: Int = 0
    open var score: Int = 0
    open var over18: Boolean = false
    open var hidden: Boolean = false
    open var thumbnail: String? = null
    open var subredditId: String? = null
    open var edited: Boolean = false
    open var downs: Int = 0
    open var saved: Boolean = false
    open var stickied: Boolean = false
    open var isSelf: Boolean = false
    open var permalink: String? = null
    open var name: String? = null
    open var created: Long = 0
    open var url: String? = null
    open var title: String? = null
    open var createdUtc: Long = 0
    open var visited: Boolean = false
    open var ups: Int = 0

    // FIXME not nice to have to set this in the data model
    override fun getViewType() = AdapterConstants.LINK
}

open class Preview : RealmObject() {
    open var images: RealmList<Images> = RealmList()
}

open class Images : RealmObject() {
    open var source: ImageMetadata? = null
    open var resolutions: RealmList<ImageMetadata> = RealmList()
    open var id: String? = null
}

open class ImageMetadata : RealmObject() {
    open var url: String? = null
    open var width: Int = 0
    open var height: Int = 0
}
