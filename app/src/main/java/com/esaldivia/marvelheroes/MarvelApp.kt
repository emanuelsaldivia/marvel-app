package com.esaldivia.marvelheroes

import android.app.Application
import com.esaldivia.marvelheroes.common.Constants
import com.esaldivia.marvelheroes.di.CoroutineModule
import com.esaldivia.marvelheroes.di.NetworkModule
import toothpick.ktp.KTP

class MarvelApp : Application() {

    override fun onCreate() {
        super.onCreate()

        KTP.openMarvelScope()
            .installModules(NetworkModule(), CoroutineModule())
    }

    override fun onTerminate() {
        super.onTerminate()

        KTP.closeScope(Constants.MARVEL_APP)
    }
}