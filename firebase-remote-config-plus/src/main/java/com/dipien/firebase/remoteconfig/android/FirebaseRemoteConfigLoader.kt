package com.dipien.firebase.remoteconfig.android

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.ListenableWorker
import com.google.android.gms.tasks.Tasks
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigClientException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

abstract class FirebaseRemoteConfigLoader(
    private val applicationContext: Context,
    private val remoteConfigParameters: List<RemoteConfigParameter>
) {

    companion object {

        private val TAG = FirebaseRemoteConfigLoader::class.simpleName

        private const val STRING_LIST_SEPARATOR = ","

        private const val REMOTE_CONFIG_STALE_STATUS_PREF_KEY = "remote_config_stale"
        private const val REMOTE_CONFIG_PUSH_TOPIC = "REMOTE_CONFIG_PUSH"
        private const val REMOTE_CONFIG_PUSH_TOPIC_SUBSCRIBED_PREF_KEY = "remote_config_push_topic_subscribed"
        private const val REMOTE_MESSAGE_STALE_KEY = "REMOTE_CONFIG_STATUS"
        private const val REMOTE_MESSAGE_STALE_VALUE = "STALE"

        private const val CRASHLYTICS_CUSTOM_KEY_PREFIX = "rc_"
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
            remoteConfigParameters.forEach {
                defaults[it.getKey()] = it.getDefaultValue()
            }
            setDefaultsAsync(defaults)
        }
    }

    protected open fun isDebuggable(applicationContext: Context): Boolean {
        val flags = applicationContext.applicationInfo.flags
        return flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    fun flagAsStale(message: RemoteMessage) {
        if (message.data[REMOTE_MESSAGE_STALE_KEY] == REMOTE_MESSAGE_STALE_VALUE) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            sharedPreferences.edit().putBoolean(REMOTE_CONFIG_STALE_STATUS_PREF_KEY, true).apply()
            Log.d(TAG, "Remote config changed to stale status")
        }
    }

    fun subscribeToRemoteConfigTopic(force: Boolean) {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (force || !sharedPreferences.getBoolean(REMOTE_CONFIG_PUSH_TOPIC_SUBSCRIBED_PREF_KEY, false)) {
            Log.i(TAG, "Subscribing to $REMOTE_CONFIG_PUSH_TOPIC FCM topic")
            FirebaseMessaging.getInstance().subscribeToTopic(REMOTE_CONFIG_PUSH_TOPIC)
            sharedPreferences.edit().putBoolean(REMOTE_CONFIG_PUSH_TOPIC_SUBSCRIBED_PREF_KEY, true).apply()
        }
    }

    fun fetchAndActivate(applicationContext: Context): ListenableWorker.Result {
        try {
            // Block on the task for a maximum of 30 seconds, otherwise time out.
            val task = remoteConfig.fetchAndActivate()
            val configParamsUpdated = Tasks.await(task, 30, TimeUnit.SECONDS)
            Log.i(TAG, "Remote config fetch. Config params updated: $configParamsUpdated")
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            sharedPreferences.edit().putBoolean(REMOTE_CONFIG_STALE_STATUS_PREF_KEY, false).apply()

            // Send remote config values as crashlytics custom keys
            val builder = CustomKeysAndValues.Builder()
            remoteConfigParameters.filter { it.trackAsCrashlyticsCustomKey() }.forEach {
                builder.putString("${CRASHLYTICS_CUSTOM_KEY_PREFIX}${it.getKey()}", getString(it))
            }
            FirebaseCrashlytics.getInstance().setCustomKeys(builder.build())

            return ListenableWorker.Result.success()
        } catch (e: ExecutionException) {
            // The Task failed, this is the same exception you'd get in a non-blocking failure handler.
            return if (e.cause is FirebaseRemoteConfigClientException && e.cause?.cause is IOException) {
                Log.d(TAG, "IOException when fetching remote config", e)
                ListenableWorker.Result.retry()
            } else {
                Log.e(TAG, e.message, e.cause ?: e)
                FirebaseCrashlytics.getInstance().recordException(e.cause ?: e)
                ListenableWorker.Result.failure()
            }
        } catch (e: InterruptedException) {
            // An interrupt occurred while waiting for the task to complete.
            Log.e(TAG, e.message, e)
            return ListenableWorker.Result.retry()
        } catch (e: TimeoutException) {
            // Task timed out before it could complete.
            Log.e(TAG, e.message, e)
            return ListenableWorker.Result.retry()
        }
    }

    fun getString(remoteConfigParameter: RemoteConfigParameter): String {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString()
        }
        return firebaseRemoteConfigValue.asString()
    }

    fun getStringList(remoteConfigParameter: RemoteConfigParameter): List<String> {
        return getString(remoteConfigParameter).split(STRING_LIST_SEPARATOR)
    }

    fun getBoolean(remoteConfigParameter: RemoteConfigParameter): Boolean {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString().toBoolean()
        }
        return firebaseRemoteConfigValue.asBoolean()
    }

    fun getLong(remoteConfigParameter: RemoteConfigParameter): Long {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString().toLong()
        }
        return firebaseRemoteConfigValue.asLong()
    }

    fun getDouble(remoteConfigParameter: RemoteConfigParameter): Double {
        val firebaseRemoteConfigValue = getValue(remoteConfigParameter)
        if (firebaseRemoteConfigValue.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return remoteConfigParameter.getDefaultValue().toString().toDouble()
        }
        return firebaseRemoteConfigValue.asDouble()
    }

    private fun getValue(parameter: RemoteConfigParameter): FirebaseRemoteConfigValue {
        val configValue = remoteConfig.getValue(parameter.getKey())
        Log.i(TAG, "Loaded Firebase Remote Config Parameter. Key [${parameter.getKey()}] | Value [${configValue.asString()}] | Source [${configValue.source}]")
        return configValue
    }
}
