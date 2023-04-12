package com.tamigo.viewModel

import androidx.lifecycle.ViewModel
import com.tamigo.navigation.MainRouter
import com.tamigo.preferences.Preferences
import com.tamigo.ui.home.HomeScreen

abstract class RegistrationViewModel : ViewModel() {
    abstract fun navigateToHomeFragment()
    abstract fun saveInfoAboutTami(name: String, skin: Int)
}

class RegistrationViewModelImpl(
    private val preferences: Preferences,
    private val router: MainRouter
) : RegistrationViewModel() {

    override fun navigateToHomeFragment() {
        router.navigate(HomeScreen())
    }

    override fun saveInfoAboutTami(name: String, skin: Int) {
        preferences.setTamiName(name)
        preferences.setTamiSkin(skin)
    }

}
