package com.tamigo.ui.registration

import androidx.fragment.app.FragmentManager
import com.tamigo.navigation.Screen

class RegistrationScreen : Screen() {
    override val tag: String = RegistrationScreen::class.java.simpleName

    override fun execute(fragmentManager: FragmentManager) {
        fragmentManager.replace(RegistrationFragment())
    }
}
