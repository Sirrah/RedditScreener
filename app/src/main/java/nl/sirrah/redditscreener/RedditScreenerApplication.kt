package nl.sirrah.redditscreener

import android.app.Application
import android.content.Context
import com.facebook.common.logging.FLog
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import io.realm.Realm
import io.realm.RealmConfiguration
import nl.sirrah.redditscreener.api.RedditRealmMigration
import nl.sirrah.redditscreener.api.Services
import java.util.*

class RedditScreenerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initializeRealm()
        initializeFresco()
    }

    private fun initializeRealm() {
        Realm.init(this)

        // Create configuration and reset Realm
        val realmConfig = RealmConfiguration.Builder()
            .schemaVersion(BuildConfig.REALM_DATABASE_VERSION)
            .migration(RedditRealmMigration())
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    private fun initializeFresco() {
        val context: Context = this

        // Setup up logger for Fresco
        val requestListeners = HashSet<RequestListener>()
        requestListeners.add(RequestLoggingListener())

        val config = OkHttpImagePipelineConfigFactory
            .newBuilder(context, Services.okHttpClient)
            .setRequestListeners(requestListeners)
            .build()

        // Initialize Fresco, including logging
        Fresco.initialize(context, config)
        FLog.setMinimumLoggingLevel(if (BuildConfig.DEBUG) FLog.VERBOSE else FLog.ERROR)
    }
}