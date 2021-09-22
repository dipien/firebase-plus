package com.dipien.firebase.remoteconfig.android

import com.dipien.remoteconfig.RemoteConfigLoader
import com.dipien.remoteconfig.RemoteConfigParameter

class FirebaseRemoteConfigLoader: RemoteConfigLoader {

    override fun fetch() {
        TODO("Not yet implemented")
    }

    override fun getObject(remoteConfigParameter: RemoteConfigParameter): Any? {
        TODO("Not yet implemented")
    }

    override fun getString(remoteConfigParameter: RemoteConfigParameter): String? {
        TODO("Not yet implemented")
    }

    override fun getStringList(remoteConfigParameter: RemoteConfigParameter): List<String>? {
        TODO("Not yet implemented")
    }

    override fun getBoolean(remoteConfigParameter: RemoteConfigParameter): Boolean? {
        TODO("Not yet implemented")
    }

    override fun getLong(remoteConfigParameter: RemoteConfigParameter): Long? {
        TODO("Not yet implemented")
    }

    override fun getDouble(remoteConfigParameter: RemoteConfigParameter): Double? {
        TODO("Not yet implemented")
    }

    override fun getRemoteConfigParameters(): List<RemoteConfigParameter> {
        // TODO
        return mutableListOf()
    }
}