package com.esaldivia.marvelheroes.di

import kotlinx.coroutines.Dispatchers
import toothpick.config.Module
import toothpick.ktp.binding.bind
import kotlin.coroutines.CoroutineContext

class CoroutineModule : Module() {

    init {
        bind<CoroutineContext>().withName("main").toInstance(Dispatchers.Main)
        bind<CoroutineContext>().withName("io").toInstance(Dispatchers.IO)
        bind<CoroutineContext>().withName("unconfined").toInstance(Dispatchers.Unconfined)
    }
}