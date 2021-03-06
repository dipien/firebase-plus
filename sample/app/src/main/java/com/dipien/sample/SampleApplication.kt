package com.dipien.sample

import android.app.Application
import androidx.work.Configuration
import com.dipien.firebase.remoteconfig.DefaultWorkerFactory
import com.dipien.firebase.remoteconfig.RemoteConfigFetcherWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SampleApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: DefaultWorkerFactory

    override fun getWorkManagerConfiguration() =
            Configuration.Builder()
                    .setWorkerFactory(workerFactory)
                    .build()

    override fun onCreate() {
        super.onCreate()

        RemoteConfigFetcherWorker.enqueue(this)
    }
}
