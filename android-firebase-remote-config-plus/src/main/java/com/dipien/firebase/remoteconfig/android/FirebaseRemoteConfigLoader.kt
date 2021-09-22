package com.dipien.firebase.remoteconfig.android

import android.annotation.SuppressLint
import android.util.Log
import com.dipien.remoteconfig.RemoteConfigLoader
import com.dipien.remoteconfig.RemoteConfigParameter
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.ktx.remoteConfig

class FirebaseRemoteConfigLoader: RemoteConfigLoader {

    companion object {
        private const val STRING_LIST_SEPARATOR = ","
    }

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig.apply {
            val defaults = mutableMapOf<String, Any>()
            getRemoteConfigParameters().forEach {
                defaults[it.getKey()] = it.getDefaultValue()
            }
            setDefaultsAsync(defaults)
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
        return getString(remoteConfigParameter).split(getStringListSeparator())
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
        Log.i(FirebaseRemoteConfigLoader::class.simpleName, "Loaded Firebase Remote Config Parameter. Key [${parameter.getKey()}] | Value [${configValue.asString()}] | Source [${configValue.source}]")
        return configValue
    }

    override fun getRemoteConfigParameters(): List<RemoteConfigParameter> {
        // TODO
        return mutableListOf()
    }

    override fun getStringListSeparator(): String {
        return STRING_LIST_SEPARATOR
    }
}