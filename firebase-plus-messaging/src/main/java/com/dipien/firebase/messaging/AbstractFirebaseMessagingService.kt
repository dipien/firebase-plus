package com.dipien.firebase.messaging

import com.dipien.firebase.remoteconfig.RemoteConfigLoader
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

abstract class AbstractFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var remoteConfigLoader: RemoteConfigLoader

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        remoteConfigLoader.flagAsStale(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        remoteConfigLoader.subscribeToRemoteConfigTopic(true)
    }
}
