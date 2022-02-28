package com.esaldivia.marvelheroes.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import toothpick.config.Module
import toothpick.ktp.binding.bind
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestDispatchersModule : Module() {

    init {
        Dispatchers.setMain(TestCoroutineDispatcher())
        bind<CoroutineContext>().withName("main").toInstance(Dispatchers.Main)
        bind<CoroutineContext>().withName("io").toInstance(Dispatchers.Main)
        bind<CoroutineContext>().withName("unconfined").toInstance(Dispatchers.Main)
    }
}