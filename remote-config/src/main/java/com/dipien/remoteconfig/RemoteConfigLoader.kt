package com.dipien.remoteconfig

interface RemoteConfigLoader {

    fun fetch()

    fun getString(remoteConfigParameter: RemoteConfigParameter): String

    fun getStringList(remoteConfigParameter: RemoteConfigParameter): List<String>

    fun getBoolean(remoteConfigParameter: RemoteConfigParameter): Boolean

    fun getLong(remoteConfigParameter: RemoteConfigParameter): Long

    fun getDouble(remoteConfigParameter: RemoteConfigParameter): Double

    fun getRemoteConfigParameters(): List<RemoteConfigParameter>

    fun getStringListSeparator(): String
}
