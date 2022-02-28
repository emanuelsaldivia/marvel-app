package com.esaldivia.marvelheroes.data.repository

import com.esaldivia.marvelheroes.network.ErrorCode
import com.esaldivia.marvelheroes.network.NetworkCallException
import com.esaldivia.marvelheroes.network.Outcome
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NetworkRepositoryTest {
    private lateinit var networkRepository: NetworkRepositoryFake
    private lateinit var response: Response<Int>

    @Before
    fun setUp() {
        response = mockk()
        networkRepository = NetworkRepositoryFake()
    }

    @Test
    fun `runNetworkCall returns Outcome Success if call is successful`() = runBlockingTest {
        every { response.isSuccessful } returns true
        every { response.body() } returns 1

        val result = networkRepository.runNetworkCall { response }

        val expected = Outcome.Success(1)
        assertTrue(result is Outcome.Success)
        val value = assertNotNull((result as Outcome.Success<Int>).value)
        assertEquals(expected, result)
        assertEquals(value, result.value)
    }

    @Test
    fun `runNetworkCall returns Outcome Error if call is unsuccessful`() = runBlockingTest {
        every { response.isSuccessful } returns false
        every { response.code() } returns 404
        every { response.message() } returns "not found"

        val result = networkRepository.runNetworkCall { response }

        val expectedDetails = NetworkCallException(404, "not found")
        val expected = Outcome.Error<Int>(ErrorCode.HTTP_FAILURE, null, expectedDetails)
        assertTrue(result is Outcome.Error)
        assertEquals(expected, result)
        assertEquals(ErrorCode.HTTP_FAILURE, (result as Outcome.Error).errorCode)
        assertNull(result.exception)
        assertEquals(expectedDetails.code, result.exceptionDetails?.code)
        assertEquals(expectedDetails.cause, result.exceptionDetails?.cause)
    }

    @Test
    fun `runNetworkCall returns Outcome Error if call exception is thrown`() = runBlockingTest {
        val exception = IOException()
        every { response.isSuccessful } throws exception

        val result = networkRepository.runNetworkCall { response }

        val expected = Outcome.Error<Int>(ErrorCode.IO_EXCEPTION, exception, null)
        assertTrue(result is Outcome.Error)
        assertEquals(expected, result)
        assertEquals(ErrorCode.IO_EXCEPTION, (result as Outcome.Error).errorCode)
        assertNull(result.exceptionDetails)
        assertTrue(result.exception is IOException)
    }

    class NetworkRepositoryFake : NetworkRepository()
}