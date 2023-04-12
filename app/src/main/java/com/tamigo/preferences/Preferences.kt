package com.tamigo.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.tamigo.R

interface Preferences {
    fun setTamiName(name: String)
    fun getTamiName(): String?
    fun setTamiSkin(skinResource: Int)
    fun getTamiSkin(): Int
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

    companion object {
        const val KEY = "tami_prefs"

        const val TAMI_NAME = "tami_name"
        const val TAMI_SKIN = "tami_skin"
    }
}
