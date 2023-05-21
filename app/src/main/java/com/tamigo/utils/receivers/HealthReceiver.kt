package com.tamigo.utils.receivers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import com.tamigo.R
import com.tamigo.ui.registration.RegistrationFragment
import com.tamigo.utils.constant.Constants.MINUS_HEALTH_INTENT
import com.tamigo.utils.constant.Constants.PLUS_HEALTH_INTENT
import com.tamigo.utils.preferences.PreferencesImpl
import com.tamigo.utils.preferences.PreferencesImpl.Companion.COINS_BALANCE
import com.tamigo.utils.preferences.PreferencesImpl.Companion.FIRST_LAUNCH
import com.tamigo.utils.preferences.PreferencesImpl.Companion.HEALTH
import com.tamigo.utils.preferences.PreferencesImpl.Companion.PRODUCTS
import com.tamigo.utils.preferences.PreferencesImpl.Companion.TAMI_NAME
import com.tamigo.utils.preferences.PreferencesImpl.Companion.TAMI_SKIN
import com.tamigo.utils.preferences.PreferencesImpl.Companion.TARGET

class HealthReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val pref = context?.getSharedPreferences(PreferencesImpl.KEY, Context.MODE_PRIVATE)
        if (pref != null) {
            when (intent?.action) {
                MINUS_HEALTH_INTENT -> {
                    minusHealth(context, pref)

                }
                PLUS_HEALTH_INTENT -> {
                    val value = intent.extras?.getInt(PLUS_HEALTH)
                    if (value != null) {
                        plusHealth(value, pref)
                    }
                }
            }
            when (pref.getFloat(HEALTH, 100f)) {
                in 90f..91f, in 80f..81f, in 70f..71f, in 60f..61f, in 50f..51f,
                in 40f..41f, in 30f..31f, in 20f..21f, in 10f..11f -> {
                    sendNotification(
                        context,
                        "${pref.getString(TAMI_NAME, APP_NAME)} має ${
                            pref.getFloat(
                                HEALTH,
                                100f
                            )
                        } здоров'я."
                    )
                }
            }
        }
    }

    private fun minusHealth(context: Context?, pref: SharedPreferences) {
        var health = pref.getFloat(HEALTH, MAX_HEALTH).toDouble()
        health -= PROPORTION
        if (health > MIN_HEALTH) {
            pref.edit { putInt(HEALTH, health.toInt()) }
            setAlarm(context)
        } else {
            clearGame(context, pref)
            if (context != null) {
                sendNotification(context, "Ти програв. Почни спочатку.")
            }
        }
    }

    private fun plusHealth(value: Int, pref: SharedPreferences) {
        var health = pref.getFloat(HEALTH, MAX_HEALTH)
        health += value
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH
        }
        pref.edit { putFloat(HEALTH, health) }
    }

    private fun clearGame(context: Context?, pref: SharedPreferences) {
        pref.edit {
            putString(TAMI_NAME, "")
            putInt(TAMI_SKIN, 0)
            putString(PRODUCTS, "")
            putInt(COINS_BALANCE, 0)
            putString(TARGET, "")
            putBoolean(FIRST_LAUNCH, true)
            putFloat(HEALTH, 100f)
        }
        context?.startActivity(Intent(context, RegistrationFragment::class.java))
    }

    private fun sendNotification(context: Context, text: String) {
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(APP_NAME)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private const val MAX_HEALTH = 100f
        private const val MIN_HEALTH = 0f
        private const val FINAL_TIME = 72f
        private const val PROPORTION = MAX_HEALTH / FINAL_TIME
        private const val CHANNEL_ID = "tami_notification_channel_id"
        private const val CHANNEL_NAME = "TamiGo Channel"
        private const val APP_NAME = "TamiGo"
        private const val NOTIFICATION_ID = 1

        const val PLUS_HEALTH = "plus_health"

        @SuppressLint("UnspecifiedImmutableFlag")
        fun setAlarm(context: Context?) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val alarmIntent = Intent(context, HealthReceiver::class.java)
            alarmIntent.action = MINUS_HEALTH_INTENT
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            val triggerTime = System.currentTimeMillis() + (60 * 60 * 1000)
            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }
}