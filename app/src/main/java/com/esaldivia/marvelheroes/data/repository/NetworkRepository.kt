package com.esaldivia.marvelheroes.data.repository

import com.esaldivia.marvelheroes.network.ErrorCode
import com.esaldivia.marvelheroes.network.NetworkCallException
import com.esaldivia.marvelheroes.network.Outcome
import retrofit2.Response
import java.io.IOException

abstract class NetworkRepository {
    suspend fun <T> runNetworkCall(block: suspend () -> Response<T>): Outcome<T> {
        try {
            val response = block()
            if (!response.isSuccessful) {
                return Outcome.Error(
                    ErrorCode.HTTP_FAILURE,
                    exceptionDetails = NetworkCallException(response.code(), response.message())
                )
            }
            return Outcome.Success(response.body())
        } catch (e: IOException) {
            return Outcome.Error(ErrorCode.IO_EXCEPTION, e)
        }
    }
}