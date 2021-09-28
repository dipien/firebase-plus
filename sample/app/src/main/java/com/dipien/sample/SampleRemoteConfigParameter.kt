package com.dipien.sample

import java.util.Locale


enum class SampleRemoteConfigParameter constructor(private val defaultValue: Any) : RemoteConfigParameter {

    STRING_PARAM("default_string_value"),
    LONG_PARAM(1L);

    override fun getKey(): String {
        return name.toLowerCase(Locale.US)
    }

    override fun getDefaultValue(): Any {
        return defaultValue
    }
}