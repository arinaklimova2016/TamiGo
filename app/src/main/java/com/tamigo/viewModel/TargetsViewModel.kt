package com.tamigo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamigo.data.Target
import com.tamigo.managers.HealthConnectManager
import com.tamigo.preferences.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class TargetsViewModel : ViewModel() {
    abstract val currentSteps: LiveData<Long>
    abstract fun getSteps()
    abstract fun setTarget(target: Target)
    abstract fun getTarget(): Target?
    abstract fun setCoinsToBalance(coins: Int?)
}

class TargetsViewModelImpl(
    private val healthConnectManager: HealthConnectManager,
    private val preferences: Preferences
) : TargetsViewModel() {
    override val currentSteps = MutableLiveData<Long>()

    override fun getSteps() {
        viewModelScope.launch(Dispatchers.IO) {
            currentSteps.postValue(healthConnectManager.getSteps())
        }
    }

    override fun setTarget(target: Target) {
        preferences.setTarget(target)
    }

    override fun getTarget(): Target? {
        return preferences.getTarget()
    }

    override fun setCoinsToBalance(coins: Int?) {
        if (coins != null)
            preferences.setCoinsToBalance(coins)
    }

}