package com.tamigo.utils.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tamigo.navigation.MainRouter
import com.tamigo.ui.food.FoodScreen
import com.tamigo.ui.registration.RegistrationScreen
import com.tamigo.ui.targets.TargetsScreen
import com.tamigo.utils.preferences.Preferences
import com.tamigo.utils.preferences.PreferencesImpl

abstract class HomeViewModel : ViewModel() {
    abstract val health: MutableLiveData<Float>
    abstract fun getTamiName(): String
    abstract fun getTamiSkin(): Int
    abstract fun navigateToRegistrationScreen()
    abstract fun openTargetsFragment()
    abstract fun openShopFragment()
    abstract fun getCoins(): String
    abstract fun isNeedStartAlarm(): Boolean
    abstract fun registerPrefsListener()
    abstract fun unregisterPrefsListener()
    abstract fun getHealthFromPref(): Float
}

class HomeViewModelImpl(
    private val preferences: Preferences,
    private val router: MainRouter,
) : HomeViewModel() {
    override val health = MutableLiveData<Float>()

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == PreferencesImpl.HEALTH) {
            health.value = preferences.getHealth()
        }
    }

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
        router.navigate(FoodScreen())
    }

    override fun getCoins(): String {
        return preferences.getCoinsBalance().toString()
    }

    override fun isNeedStartAlarm(): Boolean {
        val isNeed = preferences.isFirstLaunch()
        if (isNeed) {
            preferences.setFirstLaunch(false)
            preferences.setHealth(100f)
        }
        return isNeed
    }

    override fun registerPrefsListener() {
        preferences.registerListener(listener)
    }

    override fun unregisterPrefsListener() {
        preferences.unregisterListener(listener)
    }

    override fun getHealthFromPref(): Float {
        return preferences.getHealth()
    }
}
