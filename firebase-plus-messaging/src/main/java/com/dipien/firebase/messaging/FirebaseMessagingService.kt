package com.dipien.firebase.messaging

import com.dipien.firebase.remoteconfig.RemoteConfigLoader
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

open class FirebaseMessagingService(private val remoteConfigLoader: RemoteConfigLoader) : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        remoteConfigLoader.flagAsStale(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        remoteConfigLoader.subscribeToRemoteConfigTopic(true)
    }
}
