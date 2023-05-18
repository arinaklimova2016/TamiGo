package com.tamigo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tamigo.navigation.MainRouter
import com.tamigo.preferences.Preferences
import com.tamigo.ui.registration.RegistrationScreen
import com.tamigo.ui.shop.ShopScreen
import com.tamigo.ui.targets.TargetsScreen

abstract class HomeViewModel : ViewModel() {
    abstract val currentHealth: MutableLiveData<Double>
    abstract fun getTamiName(): String
    abstract fun getTamiSkin(): Int
    abstract fun navigateToRegistrationScreen()
    abstract fun openTargetsFragment()
    abstract fun openShopFragment()
    abstract fun getCoins(): String
    abstract fun updateHealth(health: Double)
    abstract fun getHealth(): Int
    abstract fun isNeedStartService(): Boolean
}

class HomeViewModelImpl(
    private val preferences: Preferences,
    private val router: MainRouter,
) : HomeViewModel() {

    override val currentHealth = MutableLiveData<Double>()

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

    override fun updateHealth(health: Double) {
        currentHealth.postValue(health)
    }

    override fun getHealth(): Int {
        return currentHealth.value?.toInt() ?: 0
    }

    override fun isNeedStartService(): Boolean {
        val isNeed = preferences.isFirstLaunch()
        if (isNeed) {
            preferences.setFirstLaunch(false)
        }
        return isNeed
    }

}
