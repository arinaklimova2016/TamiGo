package com.tamigo.main

import androidx.lifecycle.ViewModel
import com.tamigo.managers.HealthConnectManager
import com.tamigo.navigation.MainRouter
import com.tamigo.preferences.Preferences
import com.tamigo.ui.home.HomeScreen
import com.tamigo.ui.registration.RegistrationScreen

abstract class MainViewModel : ViewModel() {
    abstract fun navigate()
    abstract fun checkHealthConnectStatusAndPermissions()
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

}
