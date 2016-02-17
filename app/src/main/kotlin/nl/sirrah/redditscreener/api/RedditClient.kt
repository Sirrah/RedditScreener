package nl.sirrah.redditscreener.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import nl.sirrah.redditscreener.BuildConfig
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

class RedditClient {
    companion object {
        val BASE_URL = "https://www.reddit.com"

        fun create(): RedditAPI {
            val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapterFactory(ItemTypeAdapterFactory())
                    .create()

            val okHttpClient = OkHttpClient()

            // Add logging when in debug mode
            if (BuildConfig.DEBUG) {
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BODY
                okHttpClient.interceptors().add(logger)
            }

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()

            return retrofit.create(RedditAPI::class.java)
        }
    }

}