package com.dipien.sample

import com.dipien.firebase.remoteconfig.RemoteConfigParameter

enum class SampleRemoteConfigParameter constructor(private val defaultValue: Any) : RemoteConfigParameter {

    STRING_PARAM("default_string_value"),
    LONG_PARAM(1L);

    override fun getKey(): String {
        return name.lowercase()
    }

    override fun getDefaultValue(): Any {
        return defaultValue
    }
}
