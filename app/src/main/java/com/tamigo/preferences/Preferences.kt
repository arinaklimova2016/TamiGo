package com.tamigo.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.tamigo.R
import com.tamigo.data.Target

interface Preferences {
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
}

class PreferencesImpl(
    private val prefs: SharedPreferences
) : Preferences {

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

    companion object {
        const val KEY = "tami_prefs"

        const val TAMI_NAME = "tami_name"
        const val TAMI_SKIN = "tami_skin"
        const val TARGET = "target"
        const val COINS_BALANCE = "coins_balance"
        const val PRODUCTS = "products_inventory"
    }
}
