package com.esaldivia.marvelheroes.usecase

import com.esaldivia.marvelheroes.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest

class SecurityUseCase {

    fun md5Encryption(timeStamp: Long): String {
        val input = getUnencryptedHash(timeStamp)
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun getUnencryptedHash(timeStamp: Long): String {
        return timeStamp.toString() + BuildConfig.privateKey + BuildConfig.publicKey
    }
}