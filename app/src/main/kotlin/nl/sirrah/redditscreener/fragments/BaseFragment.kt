package nl.sirrah.redditscreener.fragments

import com.trello.rxlifecycle.components.support.RxFragment
import io.realm.Realm
import nl.sirrah.redditscreener.activities.MainActivity
import org.jetbrains.anko.AnkoLogger

abstract class BaseFragment : RxFragment(), AnkoLogger {

    val activity : MainActivity
        get() = super.getActivity() as MainActivity

    val realm: Realm
        get() = activity.realm!!

}