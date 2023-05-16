package com.tamigo.viewModel

import androidx.lifecycle.ViewModel
import com.tamigo.navigation.MainRouter
import com.tamigo.preferences.Preferences
import com.tamigo.ui.registration.RegistrationScreen
import com.tamigo.ui.shop.ShopScreen
import com.tamigo.ui.targets.TargetsScreen

abstract class HomeViewModel : ViewModel() {

    abstract fun getTamiName(): String
    abstract fun getTamiSkin(): Int
    abstract fun navigateToRegistrationScreen()
    abstract fun openTargetsFragment()
    abstract fun openShopFragment()
    abstract fun getCoins(): String
}

class HomeViewModelImpl(
    private val preferences: Preferences,
    private val router: MainRouter,
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

    override fun openTargetsFragment() {
        router.navigate(TargetsScreen())
    }

    override fun openShopFragment() {
        router.navigate(ShopScreen())
    }

    override fun getCoins(): String {
        return preferences.getCoinsBalance().toString()
    }

}
