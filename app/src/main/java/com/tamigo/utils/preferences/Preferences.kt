package com.tamigo.utils.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.tamigo.R
import com.tamigo.utils.data.Target
import java.time.ZonedDateTime

interface Preferences {
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun setTamiName(name: String)
    fun getTamiName(): String?
    fun setTamiSkin(skinResource: Int)
    fun getTamiSkin(): Int
    fun setTarget(target: Target)
    fun getTarget(): Target?
    fun setCoinsToBalance(coins: Int)
    fun removeCoinsFromBalance(coins: Int)
    fun getCoinsBalance(): Int
    fun setProducts(products: String)
    fun getProducts(): String?
    fun setFirstLaunch(isFirst: Boolean)
    fun isFirstLaunch(): Boolean
    fun setStartTargetTime(now: ZonedDateTime)
    fun getStartTargetTime(): String?
    fun setHealth(value: Float)
    fun getHealth(): Float
}

class PreferencesImpl(
    private val prefs: SharedPreferences
) : Preferences {
    override fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun setTamiName(name: String) {
        prefs.edit {
            putString(TAMI_NAME, name)
        }
    }

    override fun getTamiName(): String? {
        return prefs.getString(TAMI_NAME, "")
    }

    override fun setTamiSkin(skinResource: Int) {
        prefs.edit {
            putInt(TAMI_SKIN, skinResource)
        }
    }

    override fun getTamiSkin(): Int {
        return prefs.getInt(TAMI_SKIN, R.drawable.tami_blue)
    }

    override fun setTarget(target: Target) {
        prefs.edit {
            putString(TARGET, Gson().toJson(target))
        }
    }

    override fun getTarget(): Target? {
        return Gson().fromJson(prefs.getString(TARGET, ""), Target::class.java)
    }

    override fun setCoinsToBalance(coins: Int) {
        prefs.edit {
            putInt(COINS_BALANCE, prefs.getInt(COINS_BALANCE, 0) + coins)
        }
    }

    override fun removeCoinsFromBalance(coins: Int) {
        prefs.edit {
            putInt(COINS_BALANCE, prefs.getInt(COINS_BALANCE, 0) - coins)
        }
    }

    override fun getCoinsBalance(): Int {
        return prefs.getInt(COINS_BALANCE, 0)
    }

    override fun setProducts(products: String) {
        prefs.edit {
            putString(PRODUCTS, products)
        }
    }

    override fun getProducts(): String? {
        return prefs.getString(PRODUCTS, "[{}]")
    }

    override fun setFirstLaunch(isFirst: Boolean) {
        prefs.edit {
            putBoolean(FIRST_LAUNCH, isFirst)
        }
    }

    override fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(FIRST_LAUNCH, true)
    }

    override fun setStartTargetTime(now: ZonedDateTime) {
        prefs.edit {
            putString(START_TARGET, now.toString())
        }
    }

    override fun getStartTargetTime(): String? {
        return prefs.getString(START_TARGET, "")
    }

    override fun setHealth(value: Float) {
        prefs.edit {
            putFloat(HEALTH, value)
        }
    }

    override fun getHealth(): Float {
        return prefs.getFloat(HEALTH, 100f)
    }

    companion object {
        const val KEY = "tami_prefs"

        const val TAMI_NAME = "tami_name"
        const val TAMI_SKIN = "tami_skin"
        const val TARGET = "target"
        const val COINS_BALANCE = "coins_balance"
        const val PRODUCTS = "products_inventory"
        const val FIRST_LAUNCH = "is_first_launch"
        const val START_TARGET = "time_when_start_target"
        const val HEALTH = "tami_health"
    }
}
