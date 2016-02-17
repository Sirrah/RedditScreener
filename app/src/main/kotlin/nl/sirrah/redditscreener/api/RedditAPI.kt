package nl.sirrah.redditscreener.api

import retrofit.http.GET
import retrofit.http.Path
import rx.Observable

interface RedditAPI {

    @GET("/r/{subreddit}.json")
    fun listing(@Path("subreddit") subreddit: String): Observable<Listing>
}
