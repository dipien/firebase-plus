package com.dipien.firebase.remoteconfig.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.preference.PreferenceManager
import com.dipien.remoteconfig.RemoteConfigLoader
import com.dipien.remoteconfig.RemoteConfigParameter
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

abstract class FirebaseRemoteConfigLoader(private val applicationContext: Context) : RemoteConfigLoader {

    companion object {
        private val TAG = FirebaseRemoteConfigLoader::class.simpleName
        private const val STRING_LIST_SEPARATOR = ","
        const val REMOTE_CONFIG_STALE_STATUS_PREF_KEY = "remote_config_stale"
        private const val REMOTE_CONFIG_PUSH_TOPIC = "REMOTE_CONFIG_PUSH"
        private const val REMOTE_CONFIG_PUSH_TOPIC_SUBSCRIBED_PREF_KEY = "remote_config_push_topic_subscribed"
    }

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig.apply {

            setConfigSettingsAsync(remoteConfigSettings {
                val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                if (sharedPreferences.getBoolean(REMOTE_CONFIG_STALE_STATUS_PREF_KEY, false) || isDebuggable(applicationContext)) {
                    minimumFetchIntervalInSeconds = 0
                }
            })

            val defaults = mutableMapOf<String, Any>()
            getRemoteConfigParameters().forEach {
                defaults[it.getKey()] = it.getDefaultValue()
            }
            setDefaultsAsync(defaults)
        }
    }

    protected open fun isDebuggable(applicationContext: Context): Boolean {
        val flags = applicationContext.applicationInfo.flags
        return flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    @SuppressLint("LogCall")
    fun flagAsStale() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPreferences.edit().putBoolean(REMOTE_CONFIG_STALE_STATUS_PREF_KEY, true).apply()
        Log.d(TAG, "Remote config changed to stale status")
    }

    @SuppressLint("LogCall")
    fun subscribeToRemoteConfigTopic(force: Boolean) {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (force || !sharedPreferences.getBoolean(REMOTE_CONFIG_PUSH_TOPIC_SUBSCRIBED_PREF_KEY, false)) {
            Log.i(TAG, "Subscribing to $REMOTE_CONFIG_PUSH_TOPIC FCM topic")
            FirebaseMessaging.getInstance().subscribeToTopic(REMOTE_CONFIG_PUSH_TOPIC)
            sharedPreferences.edit().putBoolean(REMOTE_CONFIG_PUSH_TOPIC_SUBSCRIBED_PREF_KEY, true).apply()
        }
    }

    override fun fetch() {
        TODO("Not yet implemented")
    }

    override fun getString(remoteConfigParameter: RemoteConfigParameter): String {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString()
        }
        return firebaseRemoteConfigValue.asString()
    }

    override fun getStringList(remoteConfigParameter: RemoteConfigParameter): List<String> {
        return getString(remoteConfigParameter).split(STRING_LIST_SEPARATOR)
    }

    override fun getBoolean(remoteConfigParameter: RemoteConfigParameter): Boolean {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString().toBoolean()
        }
        return firebaseRemoteConfigValue.asBoolean()
    }

    override fun getLong(remoteConfigParameter: RemoteConfigParameter): Long {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString().toLong()
        }
        return firebaseRemoteConfigValue.asLong()
    }

    override fun getDouble(remoteConfigParameter: RemoteConfigParameter): Double {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString().toDouble()
        }
        return firebaseRemoteConfigValue.asDouble()
    }

    @SuppressLint("LogCall")
    private fun getValue(parameter: RemoteConfigParameter): FirebaseRemoteConfigValue {
        val configValue = remoteConfig.getValue(parameter.getKey())
        Log.i(TAG, "Loaded Firebase Remote Config Parameter. Key [${parameter.getKey()}] | Value [${configValue.asString()}] | Source [${configValue.source}]")
        return configValue
    }
}
