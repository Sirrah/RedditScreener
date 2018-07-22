package nl.sirrah.redditscreener.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.realm.Realm
import nl.sirrah.redditscreener.R
import nl.sirrah.redditscreener.fragments.OverviewFragment
import org.jetbrains.anko.find

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = find(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            changeFragment(OverviewFragment())
        }
    }

    fun changeFragment(fragment: Fragment, cleanStack: Boolean = false) {
        if (cleanStack) {
            clearBackStack()
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.abc_fade_in, R.anim.abc_fade_out,
                R.anim.abc_popup_enter, R.anim.abc_popup_exit
            )
            .replace(R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onBackPressed() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 1) {
            manager.popBackStack()
        } else {
            finish()
        }
    }

    private var _realm: Realm? = null
    val realm: Realm
        get() {
            if (_realm == null || _realm!!.isClosed) {
                _realm = Realm.getDefaultInstance()
            }
            return _realm!!
        }

    override fun onStop() {
        super.onStop()

        _realm?.apply {
            removeAllChangeListeners()
            close()
        }
        _realm = null
    }
}
