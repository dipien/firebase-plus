package com.dipien.firebase.remoteconfig.android

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

abstract class AbstractFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        getFirebaseRemoteConfigLoader().flagAsStale(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        getFirebaseRemoteConfigLoader().subscribeToRemoteConfigTopic(true)
    }

    abstract fun getFirebaseRemoteConfigLoader(): FirebaseRemoteConfigLoader
}