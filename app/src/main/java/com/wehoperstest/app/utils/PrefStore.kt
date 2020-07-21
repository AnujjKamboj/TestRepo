package com.wehoperstest.app.utils


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList
/**
 *A simple created by Anuj Kamboj.
 *
 */
class PrefStore(private val mAct: Context) {

    private val pref: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(mAct)

    fun cleanPref() {
        val settings = pref
        settings.edit().clear().apply()
    }

    fun containValue(key: String): Boolean {
        val settings = pref
        return settings.contains(key)
    }

    @JvmOverloads
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val settings = pref
        return settings.getBoolean(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        val settings = pref
        val editor = settings.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun saveString(key: String, value: String) {
        val settings = pref
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @JvmOverloads
    fun getString(key: String, defaultVal: String? = null): String? {
        val settings = pref
        return settings.getString(key, defaultVal)
    }


    fun saveLong(key: String, value: Long) {
        val settings = pref
        val editor = settings.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    @JvmOverloads
    fun getLong(key: String, defaultVal: Long = 0): Long {
        val settings = pref
        return settings.getLong(key, defaultVal)
    }

    fun setInt(subscription_id: String, sbu_id: Int) {
        val settings = pref
        val editor = settings.edit()
        editor.putInt(subscription_id, sbu_id)
        editor.apply()
    }

    @JvmOverloads
    fun getInt(key: String, defaultVal: Int = 0): Int {
        val settings = pref
        return settings.getInt(key, defaultVal)
    }

    fun <T> getObject(key: String, a: Class<T>): T? {
        val settings = pref
        val gson = settings.getString(key, null)
        return if (gson == null) {
            null
        } else {
            try {
                GSON.fromJson(gson, a)
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "Object storaged with key "
                            + key + " is instanceof other class"
                )
            }

        }
    }

    // to save object in prefrence
    fun save(key: String?, `object`: Any?) {
        if (`object` == null) {
            throw IllegalArgumentException("object is null")
        }

        if (key == "" || key == null) {
            throw IllegalArgumentException("key is empty or null")
        }
        val settings = pref
        val editor = settings.edit()
        editor.putString(key, GSON.toJson(`object`)).apply()
    }

    companion object {

        private val GSON = Gson()
        private val gson = Gson()

        fun getStringFromArray(strings: ArrayList<String>): String {
            return gson.toJson(strings)
        }

        fun getArrayFromString(time: String): ArrayList<String>? {
            val type = object : TypeToken<ArrayList<String>>() {

            }.type
            return gson.fromJson<ArrayList<String>>(time, type)
        }
    }

}