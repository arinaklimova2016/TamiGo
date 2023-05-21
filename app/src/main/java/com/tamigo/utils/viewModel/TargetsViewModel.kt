package com.tamigo.utils.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamigo.utils.data.Target
import com.tamigo.utils.managers.HealthConnectManager
import com.tamigo.utils.preferences.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

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
        preferences.setStartTargetTime(ZonedDateTime.now())
        getSteps()
    }

    override fun getTarget(): Target? {
        return preferences.getTarget()
    }

    override fun setCoinsToBalance(coins: Int?) {
        if (coins != null)
            preferences.setCoinsToBalance(coins)
    }
}