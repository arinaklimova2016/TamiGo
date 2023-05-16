package com.tamigo.ui.shop

import androidx.fragment.app.FragmentManager
import com.tamigo.R
import com.tamigo.navigation.Screen

class ShopScreen : Screen() {
    override val tag: String = ShopScreen::class.java.simpleName

    override fun execute(fragmentManager: FragmentManager) {
        fragmentManager.replace(ShopFragment(), container = R.id.home_fragment_container)
    }
}