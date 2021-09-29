package com.dipien.firebase.remoteconfig

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.DelegatingWorkerFactory
import javax.inject.Inject
import javax.inject.Singleton

// https://medium.com/androiddevelopers/customizing-workmanager-with-dagger-1029688c0978
@Singleton
class DefaultWorkerFactory @Inject constructor(
    hiltWorkerFactory: HiltWorkerFactory,
    remoteConfigLoader: RemoteConfigLoader
) : DelegatingWorkerFactory() {
    init {
        addFactory(RemoteConfigWorkerFactory(remoteConfigLoader))
        addFactory(hiltWorkerFactory)
    }
}
