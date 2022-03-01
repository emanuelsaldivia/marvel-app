package com.esaldivia.marvelheroes.di

import com.esaldivia.marvelheroes.openMarvelScope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import kotlin.coroutines.CoroutineContext

object CoroutineDispatcher {
    val Main: CoroutineContext by inject("main")
    val IO: CoroutineContext by inject("io")
    val Unconfined: CoroutineContext by inject("unconfined")

    init {
        KTP.openMarvelScope().inject(this)
    }
}