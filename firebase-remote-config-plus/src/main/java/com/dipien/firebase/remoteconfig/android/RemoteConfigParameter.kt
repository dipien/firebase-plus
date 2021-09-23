package com.dipien.firebase.remoteconfig.android

interface RemoteConfigParameter {

    fun getKey(): String

    fun getDefaultValue(): Any
}
