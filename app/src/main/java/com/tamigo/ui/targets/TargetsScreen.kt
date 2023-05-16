package com.tamigo.ui.targets

import androidx.fragment.app.FragmentManager
import com.tamigo.R
import com.tamigo.navigation.Screen

class TargetsScreen : Screen() {
    override val tag: String = TargetsScreen::class.java.simpleName

    override fun execute(fragmentManager: FragmentManager) {
        fragmentManager.replace(TargetsFragment(), container = R.id.home_fragment_container)
    }
}