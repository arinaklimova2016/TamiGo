package com.tamigo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tamigo.interfase.UpdateServiceListener
import com.tamigo.viewModel.HomeViewModel
import org.koin.android.ext.android.inject
import java.util.*


class HealthService : Service(), UpdateServiceListener {

    private val homeViewModel: HomeViewModel by inject()

    private val timer = Timer()
    private var currentValue: Double = 100.0

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                currentValue -= PROPORTION
                updateHealth()
            }
        }, 0, 60000)
        return START_STICKY
    }
//    1 * 60 * 60 * 1000
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    fun updateHealth() {
        homeViewModel.updateHealth(currentValue)
    }

    private fun updateCurrentHealth(value: Double) {
        currentValue += value
        if (currentValue > 100.0) {
            currentValue = 100.0
        }
    }

    companion object {
        private const val MAX_HEALTH = 100
        private const val MIN_HEALTH = 0
        private const val FINAL_TIME = 72
        private const val PROPORTION = MAX_HEALTH.toDouble() / FINAL_TIME.toDouble()
    }

    override fun updateCurrentHealthInService(value: Double) {
        updateCurrentHealth(value)
        updateHealth()
    }
}