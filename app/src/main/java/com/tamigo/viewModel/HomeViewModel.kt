package com.tamigo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamigo.managers.HealthConnectManager
import com.tamigo.navigation.MainRouter
import com.tamigo.preferences.Preferences
import com.tamigo.ui.registration.RegistrationScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class HomeViewModel : ViewModel() {
    abstract val currentSteps: LiveData<Long>
    abstract fun getTamiName(): String
    abstract fun getTamiSkin(): Int
    abstract fun navigateToRegistrationScreen()
    abstract fun getSteps()
    abstract fun recordSteps()
}

class HomeViewModelImpl(
    private val preferences: Preferences,
    private val router: MainRouter,
    private val healthConnectManager: HealthConnectManager
) : HomeViewModel() {
    override val currentSteps = MutableLiveData<Long>()
    override fun getTamiName(): String {
        return preferences.getTamiName() ?: "Tami"
    }

    override fun getTamiSkin(): Int {
        return preferences.getTamiSkin()
    }

    override fun navigateToRegistrationScreen() {
        router.navigate(RegistrationScreen())
    }

    override fun getSteps() {
        viewModelScope.launch(Dispatchers.IO) {
            currentSteps.postValue(healthConnectManager.getSteps())
        }
    }

    override fun recordSteps() {
        viewModelScope.launch(Dispatchers.IO) {
            healthConnectManager.insertData(123)
        }
    }

}
