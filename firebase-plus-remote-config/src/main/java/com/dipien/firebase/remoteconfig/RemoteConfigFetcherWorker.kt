package com.dipien.firebase.remoteconfig

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.IOException

class RemoteConfigFetcherWorker constructor(
    private val appContext: Context,
    workerParams: WorkerParameters,
    private val remoteConfigLoader: RemoteConfigLoader
) : Worker(appContext, workerParams) {

    companion object {

        private val TAG = RemoteConfigFetcherWorker::class.simpleName

        private val workRequestBuilder by lazy {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            OneTimeWorkRequestBuilder<RemoteConfigFetcherWorker>().setConstraints(constraints)
        }

        fun enqueue(context: Context) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                RemoteConfigFetcherWorker::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                workRequestBuilder.build()
            )
        }
    }

    override fun doWork(): Result {
        Log.d(TAG, "Executing worker")
        return try {
            remoteConfigLoader.fetchAndActivate()
        } catch (e: Throwable) {
            val result = getResult(e)
            if (result is Result.Failure) {
                Log.e(TAG, e.message, e)
                FirebaseCrashlytics.getInstance().recordException(e)
            } else {
                Log.d(TAG, "Work failed. Returning retry result", e)
            }
            result
        }
    }

    private fun getResult(throwable: Throwable): Result {
        return if (throwable is IOException || (throwable.cause != null && throwable.cause is IOException)) Result.retry() else Result.failure()
    }
}
