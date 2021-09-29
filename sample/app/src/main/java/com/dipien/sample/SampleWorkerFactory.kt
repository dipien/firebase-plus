package com.dipien.sample

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.DelegatingWorkerFactory
import com.dipien.firebase.remoteconfig.RemoteConfigLoader
import com.dipien.firebase.remoteconfig.RemoteConfigWorkerFactory
import javax.inject.Inject
import javax.inject.Singleton

// https://medium.com/androiddevelopers/customizing-workmanager-with-dagger-1029688c0978
@Singleton
class SampleWorkerFactory @Inject constructor(
    hiltWorkerFactory: HiltWorkerFactory,
    remoteConfigLoader: RemoteConfigLoader
) : DelegatingWorkerFactory() {
    init {
        addFactory(RemoteConfigWorkerFactory(remoteConfigLoader))
        addFactory(hiltWorkerFactory)
    }
}
