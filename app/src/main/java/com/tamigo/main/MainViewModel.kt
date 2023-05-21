package com.tamigo.main

import androidx.lifecycle.ViewModel
import com.tamigo.utils.data.Target
import com.tamigo.utils.managers.HealthConnectManager
import com.tamigo.navigation.MainRouter
import com.tamigo.utils.preferences.Preferences
import com.tamigo.ui.home.HomeScreen
import com.tamigo.ui.info.InfoScreen
import com.tamigo.ui.registration.RegistrationScreen

abstract class MainViewModel : ViewModel() {
    abstract fun navigate()
    abstract fun checkHealthConnectStatusAndPermissions()
    abstract fun navigateInfo()
    abstract fun onClickRestart()
}

class MainViewModelImpl(
    private val router: MainRouter,
    private val preferences: Preferences,
    private val healthConnectManager: HealthConnectManager
) : MainViewModel() {
    override fun navigate() {
        if (!preferences.getTamiName().isNullOrEmpty())
            router.navigate(HomeScreen())
        else
            router.navigate(RegistrationScreen())
    }

    override fun checkHealthConnectStatusAndPermissions() {
        healthConnectManager.checkHealthConnect()
    }

    override fun navigateInfo() {
        router.navigate(InfoScreen())
    }

    override fun onClickRestart() {
        preferences.setTamiName("")
        preferences.setTamiSkin(0)
        preferences.setProducts("")
        preferences.removeCoinsFromBalance(preferences.getCoinsBalance())
        preferences.setTarget(Target(0, 0))
        preferences.setFirstLaunch(true)
        preferences.setHealth(100f)
        router.navigate(RegistrationScreen())
    }
}
