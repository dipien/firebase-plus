package com.dipien.firebase.remoteconfig

import android.content.Context
import androidx.work.ListenableWorker
import com.google.firebase.messaging.RemoteMessage

interface RemoteConfigLoader {

    fun flagAsStale(message: RemoteMessage)

    fun subscribeToRemoteConfigTopic(force: Boolean)

    fun fetchAndActivate(applicationContext: Context): ListenableWorker.Result

    fun getString(remoteConfigParameter: RemoteConfigParameter): String

    fun getStringList(remoteConfigParameter: RemoteConfigParameter): List<String>

    fun getBoolean(remoteConfigParameter: RemoteConfigParameter): Boolean

    fun getLong(remoteConfigParameter: RemoteConfigParameter): Long

    fun getDouble(remoteConfigParameter: RemoteConfigParameter): Double
}