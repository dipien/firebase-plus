package com.dipien.firebase.remoteconfig

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class RemoteConfigWorkerFactory(
    private val remoteConfigLoader: RemoteConfigLoader
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            RemoteConfigFetcherWorker::class.java.name ->
                RemoteConfigFetcherWorker(appContext, workerParameters, remoteConfigLoader)
            else ->
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
        }
    }
}
