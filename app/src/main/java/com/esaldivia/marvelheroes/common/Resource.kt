package com.esaldivia.marvelheroes.common

sealed class Resource<T> {
    class Success<T>(val value: T) : Resource<T>()
    class Error<T>(val errorMessage: String?) : Resource<T>()
    class Loading<T> : Resource<T>()
}