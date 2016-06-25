package nl.sirrah.redditscreener.fragments

import com.trello.rxlifecycle.components.support.RxFragment
import io.realm.Realm
import nl.sirrah.redditscreener.activities.MainActivity
import org.jetbrains.anko.AnkoLogger

abstract class BaseFragment : RxFragment(), AnkoLogger {

    val realm: Realm
        get() = (activity as? MainActivity)?.realm!!

}