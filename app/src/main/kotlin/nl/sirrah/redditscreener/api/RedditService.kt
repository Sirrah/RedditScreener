package nl.sirrah.redditscreener.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import nl.sirrah.redditscreener.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface RedditService {
    companion object {
        val BASE_URL = "https://www.reddit.com/"

        val instance by lazy { create() }

        private fun create(): RedditService {
            val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapterFactory(ItemTypeAdapterFactory())
                    .create()

            val okHttpClientBuilder = OkHttpClient.Builder()

            // Add logging when in debug mode
            if (BuildConfig.DEBUG) {
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder.addInterceptor(logger)
            }

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClientBuilder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()

            return retrofit.create(RedditService::class.java)
        }
    }

    @GET("r/{subreddit}.json")
    fun listing(@Path("subreddit") subreddit: String): Observable<Listing>
}
