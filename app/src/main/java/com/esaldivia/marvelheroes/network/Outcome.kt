package com.esaldivia.marvelheroes.network

sealed class Outcome<R> {
    data class Success<R>(val value: R?) : Outcome<R>()

    data class Error<R>(
        val errorCode: ErrorCode,
        val exception: Throwable? = null,
        val exceptionDetails: NetworkCallException? = null
    ) : Outcome<R>()
}