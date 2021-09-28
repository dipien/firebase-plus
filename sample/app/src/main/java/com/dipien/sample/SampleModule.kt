package com.dipien.sample

import com.dipien.firebase.remoteconfig.FirebaseRemoteConfigLoader
import com.dipien.firebase.remoteconfig.RemoteConfigLoader
import com.dipien.firebase.remoteconfig.RemoteConfigParameter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

@Module
@InstallIn(SingletonComponent::class)
class SampleProviderModule {

    @ElementsIntoSet
    @Provides
    fun providesSampleRemoteConfigParameter(): Set<RemoteConfigParameter> = SampleRemoteConfigParameter.values().toSet()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SampleBindingModule {

    @Binds
    abstract fun bindsFirebaseRemoteConfigLoader(impl: FirebaseRemoteConfigLoader): RemoteConfigLoader
}
