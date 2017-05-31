package nl.sirrah.redditscreener.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import nl.sirrah.redditscreener.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Services {
    private val REDDIT_BASE_URL = "https://www.reddit.com/"

    val okHttpClient: OkHttpClient by lazy {
        val okHttpClientBuilder = OkHttpClient.Builder()

        // Add logging when in debug mode
        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logger)
        }

        okHttpClientBuilder.build()
    }

    val reddit: RedditApi by lazy {
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapterFactory(ItemTypeAdapterFactory())
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(REDDIT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        retrofit.create(RedditApi::class.java)
    }
}