package com.tamigo.ui.info

import androidx.fragment.app.FragmentManager
import com.tamigo.navigation.Screen

class InfoScreen:Screen() {
    override val tag: String = InfoScreen::class.java.simpleName

    override fun execute(fragmentManager: FragmentManager) {
        fragmentManager.replace(InfoFragment())
    }
}