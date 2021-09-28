package com.dipien.firebase.remoteconfig

interface RemoteConfigParameter {

    fun getKey(): String

    fun getDefaultValue(): Any

    fun trackAsCrashlyticsCustomKey(): Boolean = true
}
