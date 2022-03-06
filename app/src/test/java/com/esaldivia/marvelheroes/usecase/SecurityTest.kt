package com.esaldivia.marvelheroes.usecase

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class SecurityTest {

    private lateinit var security: SecurityUseCase

    @Before
    fun setUp() {
        security = SecurityUseCase()
    }

    @Test
    fun `getUnencryptedHash creates correct hash`() {
        val result = security.md5Encryption(1L)

        assertEquals("a571667fd08fbda87786723c10705cff", result)
    }
}