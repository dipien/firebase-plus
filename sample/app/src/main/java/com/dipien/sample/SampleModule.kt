package com.dipien.sample

import com.dipien.firebase.remoteconfig.RemoteConfigParameter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

@Module
@InstallIn(SingletonComponent::class)
class SampleModule {

    @ElementsIntoSet
    @Provides
    fun bindsSampleRemoteConfigParameter(): Set<RemoteConfigParameter> = SampleRemoteConfigParameter.values().toSet()
}
