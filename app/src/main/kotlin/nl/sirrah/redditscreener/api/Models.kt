package nl.sirrah.redditscreener.api

import nl.sirrah.redditscreener.adapters.AdapterConstants
import nl.sirrah.redditscreener.common.adapters.ViewType

data class RedditResponse(var kind: String, var data: Listing)

data class Listing(var children: List<LinkWrapper>, var after: String, var before: String?)

data class LinkWrapper (var kind: String, var data: Link)

/**
 * t3 (link) type
 *
 * TODO check which properties we need, and see if we can change them to val's
 */
data class Link(
        /* {
            "domain": "imgur.com",
            "banned_by": null,
            "media_embed": { },
            "subreddit": "aww",
            "selftext_html": null,
            "selftext": "",
            "likes": null,
            "user_reports": [ ],
            "secure_media": null,
            "link_flair_text": null,
            "id": "33017h",
            "gilded": 0,
            "archived": false,
            "clicked": false,
            "report_reasons": null,
            "author": "ShadeeLeeann",
            "num_comments": 24,
            "score": 709,
            "approved_by": null,
            "over_18": false,
            "hidden": false,
            "thumbnail": "http://b.thumbs.redditmedia.com/p7K32TmyYCxrGKh9tuLJBybY0sfq4jcvu4P4xGYtYOs.jpg",
            "subreddit_id": "t5_2qh1o",
            "edited": false,
            "link_flair_css_class": null,
            "author_flair_css_class": null,
            "downs": 0,
            "secure_media_embed": { },
            "saved": false,
            "stickied": false,
            "is_self": false,
            "permalink": "/r/aww/comments/33017h/our_cat_posey_picked_up_this_stray_kitten_while/",
            "name": "t3_33017h",
            "created": 1429360175,
            "url": "http://imgur.com/cHV0WIc",
            "author_flair_text": null,
            "title": "Our cat, Posey, picked up this stray kitten while roaming today. This was her diabolical scheme to make us keep her.",
            "created_utc": 1429331375,
            "distinguished": null,
            "media": null,
            "mod_reports": [ ],
            "visited": false,
            "num_reports": null,
            "ups": 709
         } */

        var domain: String,
        //    public String bannedBy;
        //    public String mediaEmbed;
        var subreddit: String,
        //    public String selftextHtml;
        var selfttext: String,
        //    public String likes;
        //    public String userReports;
        //    public String secureMedia;
        //    public String linkFlairText;
        var id: String,
        var gilded: Int = 0,
        var archived: Boolean = false,
        var clicked: Boolean = false,
        //    public String reportReasons;
        var author: String,
        var numComments: Int = 0,
        var score: Int = 0,
        //    public String approvedBy;
        var over18: Boolean = false,
        var hidden: Boolean = false,
        var thumbnail: String,
        var subredditId: String,
        var edited: Boolean = false,
        //    public String linkFlairCssClass;
        //    public String authorFlairCssClass;
        var downs: Int = 0,
        //    public String secureMediaEmbed;
        var saved: Boolean = false,
        var stickied: Boolean = false,
        var isSelf: Boolean = false,
        var permalink: String,
        var name: String,
        var created: Long = 0,
        var url: String,
        //    public String authorFlairText;
        var title: String,
        var createdUtc: Long = 0,
        //    public String distinguished;
        //    public String media;
        //    public String modReports;
        var visited: Boolean = false,
        //    public int numReports;s
        var ups: Int = 0
) : ViewType {

    // FIXME not nice to have to set this in the data model
    override fun getViewType() = AdapterConstants.LINK
}
