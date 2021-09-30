package com.dipien.firebase.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue

class StaticFirebaseRemoteConfigValue(private val remoteConfigParameter: RemoteConfigParameter) : FirebaseRemoteConfigValue {

    override fun asLong(): Long {
        return remoteConfigParameter.getDefaultValue().toString().toLong()
    }

    override fun asDouble(): Double {
        return remoteConfigParameter.getDefaultValue().toString().toDouble()
    }

    override fun asString(): String {
        return remoteConfigParameter.getDefaultValue().toString()
    }

    override fun asByteArray(): ByteArray {
        return remoteConfigParameter.getDefaultValue().toString().toByteArray()
    }

    override fun asBoolean(): Boolean {
        return remoteConfigParameter.getDefaultValue().toString().toBoolean()
    }

    override fun getSource(): Int {
        return FirebaseRemoteConfig.VALUE_SOURCE_STATIC
    }
}
