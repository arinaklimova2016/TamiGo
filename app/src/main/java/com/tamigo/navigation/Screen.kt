package com.tamigo.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tamigo.R

abstract class Screen {
    abstract val tag: String

    abstract fun execute(fragmentManager: FragmentManager)

    protected fun FragmentManager.replace(
        fragment: Fragment,
        @IdRes container: Int = R.id.fragment_container,
        addToBackStack: Boolean = true
    ) {
        beginTransaction()
            .replace(container, fragment, tag)
            .apply {
                if (addToBackStack) {
                    addToBackStack(tag)
                }
            }
            .commit()
    }
}
