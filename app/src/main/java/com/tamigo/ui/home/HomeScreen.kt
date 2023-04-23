package com.tamigo.ui.home

import androidx.fragment.app.FragmentManager
import com.tamigo.navigation.Screen

class HomeScreen : Screen() {
    override val tag: String = HomeScreen::class.java.simpleName

    override fun execute(fragmentManager: FragmentManager) {
        fragmentManager.replace(HomeFragment(), addToBackStack = false)
    }
}
