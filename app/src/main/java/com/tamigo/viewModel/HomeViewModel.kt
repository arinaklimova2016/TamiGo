package com.tamigo.viewModel

import androidx.lifecycle.ViewModel
import com.tamigo.navigation.MainRouter
import com.tamigo.preferences.Preferences
import com.tamigo.ui.registration.RegistrationScreen

abstract class HomeViewModel : ViewModel() {
    abstract fun getTamiName(): String
    abstract fun getTamiSkin(): Int
    abstract fun navigateToRegistrationScreen()
}

class HomeViewModelImpl(
    private val preferences: Preferences,
    private val router: MainRouter
) : HomeViewModel() {
    override fun getTamiName(): String {
        return preferences.getTamiName() ?: "Tami"
    }

    override fun getTamiSkin(): Int {
        return preferences.getTamiSkin()
    }

    override fun navigateToRegistrationScreen() {
        router.navigate(RegistrationScreen())
    }

}
