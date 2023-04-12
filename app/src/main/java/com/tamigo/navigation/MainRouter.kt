package com.tamigo.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MainRouter : Router {
    private var fragmentManager: FragmentManager? = null
    private val autodetacher: ActivityLifecycleDetacher? = null

    override fun attachTo(host: Any) {
        if (fragmentManager != null) {
            throw IllegalStateException("Router is already attached")
        }
        if (host is AppCompatActivity) {
            fragmentManager = host.supportFragmentManager
            autoDetachOnDestroy(host)
        } else {
            throw IllegalArgumentException(
                "Main router cannot be attached to instance of ${host.javaClass}. " +
                        "It has to be ${FragmentActivity::javaClass} subclass"
            )
        }
    }

    private fun autoDetachOnDestroy(activity: FragmentActivity) {
        val observer = ActivityLifecycleDetacher(activity = activity)
        activity.lifecycle.addObserver(observer)
    }

    override fun navigate(screen: Screen) {
        execute(Command.Navigate(screen))
    }

    override fun back() {
        execute(Command.Back)
    }

    private fun execute(command: Command) {
        val fragmentManager = fragmentManager ?: throw IllegalStateException(
            "Router cannot be used when fragment manager is not attached"
        )
        when (command) {
            Command.Back -> fragmentManager.popBackStackImmediate()
            is Command.Navigate -> command.screen.execute(fragmentManager)
        }
    }

    override fun detach() {
        autodetacher?.release()
        fragmentManager = null
    }

    inner class ActivityLifecycleDetacher(
        private val activity: FragmentActivity
    ) : LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                detach()
                source.lifecycle.removeObserver(this)
            }
        }

        fun release() {
            activity.lifecycle.removeObserver(this)
        }
    }
}
