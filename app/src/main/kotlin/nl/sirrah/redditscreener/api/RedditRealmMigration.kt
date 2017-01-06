package nl.sirrah.redditscreener.api

import io.realm.DynamicRealm
import io.realm.RealmMigration

class RedditRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        // Nothing to do yet
    }
}