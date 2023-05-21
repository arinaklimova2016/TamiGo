package com.tamigo.ui.food

import androidx.fragment.app.FragmentManager
import com.tamigo.R
import com.tamigo.navigation.Screen

class FoodScreen : Screen() {
    override val tag: String = FoodScreen::class.java.simpleName

    override fun execute(fragmentManager: FragmentManager) {
        fragmentManager.replace(FoodFragment(), container = R.id.home_fragment_container)
    }
}