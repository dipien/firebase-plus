package com.dipien.sample

import android.util.Log
import com.dipien.firebase.messaging.AbstractFirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleFirebaseMessagingService : AbstractFirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(SampleFirebaseMessagingService::class.java.simpleName, "Received message ${message.data}")
    }
}
