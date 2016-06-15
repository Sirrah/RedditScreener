package nl.sirrah.redditscreener.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface RedditApi {
    @GET("r/{subreddit}.json")
    fun listing(@Path("subreddit") subreddit: String,
                @Query("after") after: String = "",
                @Query("limit") limit: Int = 3)
            : Observable<Listing>
}
