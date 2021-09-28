package com.dipien.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dipien.firebase.remoteconfig.RemoteConfigLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var remoteConfigLoader: RemoteConfigLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        remoteConfigLoader.getString(SampleRemoteConfigParameter.STRING_PARAM)
        remoteConfigLoader.getLong(SampleRemoteConfigParameter.LONG_PARAM)
    }
}
