package com.esaldivia.marvelheroes.data.repository

import com.esaldivia.marvelheroes.data.model.DataNetworkDto
import com.esaldivia.marvelheroes.data.model.ImageNetworkDto
import com.esaldivia.marvelheroes.data.model.MarvelDataWrapper
import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.data.model.character.CharacterNetworkDto
import com.esaldivia.marvelheroes.network.CharacterNetworkService
import com.esaldivia.marvelheroes.network.Outcome
import com.esaldivia.marvelheroes.usecase.ImageUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject
import kotlin.test.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CharacterRepositoryTest {
    private val characterRepository: CharacterRepository by inject()
    private lateinit var characterNetworkService: CharacterNetworkService
    private lateinit var charactersResponse: Response<MarvelDataWrapper<CharacterNetworkDto>>
    private lateinit var characterResponse: Response<MarvelDataWrapper<CharacterNetworkDto>>
    private lateinit var marvelDataWrapper: MarvelDataWrapper<CharacterNetworkDto>
    private lateinit var dataNetworkDto: DataNetworkDto<CharacterNetworkDto>
    private lateinit var imageUseCase: ImageUseCase
    private val character = Character(1, "name", "description", "path")
    private val characterList: List<Character> = listOf(character)

    @Before
    fun setUp() {
        val image: ImageNetworkDto = mockImageNetworkDto()
        val characterNetworkDto: CharacterNetworkDto = mockCharacterNetworkDto(image)
        val characterNetworkDtoList: List<CharacterNetworkDto> = listOf(characterNetworkDto)
        dataNetworkDto = mockDataNetworkDto(characterNetworkDtoList)
        marvelDataWrapper = mockDataWrapper(dataNetworkDto)
        charactersResponse = mockResponse(marvelDataWrapper)
        characterResponse = mockk {
            every {
                hint(MarvelDataWrapper::class)
                body()
            } returns marvelDataWrapper
            every { isSuccessful } returns true
        }
        characterNetworkService = mockk {
            coEvery { getCharacters(any(), any(), any()) } returns charactersResponse
            coEvery { getCharacter(any(), any(), any()) } returns characterResponse
        }
        imageUseCase = mockk {
            every { getLargeLandscapeImageUri(any()) } returns "path"
        }

        KTP.openScope(CharacterRepositoryTest::class.java)
            .installTestModules(module {
                bind<CharacterNetworkService>().toInstance(characterNetworkService)
                bind<ImageUseCase>().toInstance(imageUseCase)
            }).inject(this)
    }

    @After
    fun tearDown() {
        KTP.closeScope(CharacterRepositoryTest::class.java)
    }

    @Test
    fun `getCharacters makes correct network call with default values`() = runBlockingTest {
        val result = characterRepository.getCharacters(1234, hash = "hash")

        assertTrue(result is Outcome.Success)
        assertEquals(characterList, (result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacters(1234, 0, "hash") }
    }

    @Test
    fun `getCharacters makes correct network call`() = runBlockingTest {
        val result = characterRepository.getCharacters(1234, 1,"hash")

        assertTrue(result is Outcome.Success)
        assertEquals(characterList, (result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacters(1234, 1, "hash") }
    }

    @Test
    fun `getCharacters with null body returns null`() = runBlockingTest {
        every { charactersResponse.body() } returns null

        val result = characterRepository.getCharacters(1234, hash = "hash")

        assertTrue(result is Outcome.Success)
        assertNull((result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacters(1234, 0, "hash") }
        verify { charactersResponse.body() }
        verify(exactly = 0) { marvelDataWrapper.data }
    }

    @Test
    fun `getCharacters with null data returns null`() = runBlockingTest {
        every { marvelDataWrapper.data } returns null

        val result = characterRepository.getCharacters(1234, hash = "hash")

        assertTrue(result is Outcome.Success)
        assertNull((result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacters(1234, 0, "hash") }
        verify { charactersResponse.body() }
        verify { marvelDataWrapper.data }
        verify(exactly = 0) { dataNetworkDto.results }
    }

    @Test
    fun `getCharacters with null results returns null`() = runBlockingTest {
        every { dataNetworkDto.results } returns null

        val result = characterRepository.getCharacters(1234, hash = "hash")

        assertTrue(result is Outcome.Success)
        assertNull((result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacters(1234, 0, "hash") }
        verify { charactersResponse.body() }
        verify { marvelDataWrapper.data }
        verify { dataNetworkDto.results }
    }

    @Test
    fun `getCharacters returns outcome error`() = runBlockingTest {
        every { charactersResponse.isSuccessful } returns false
        every { charactersResponse.code() } returns 404
        every { charactersResponse.message() } returns "errorMessage"

        val result = characterRepository.getCharacters(1234, hash = "hash")

        assertTrue(result is Outcome.Error)
        val details = assertNotNull((result as Outcome.Error).exceptionDetails)
        assertEquals(404, details.code)
        assertEquals("errorMessage", details.cause)
    }

    @Test
    fun `getCharacter returns outcome success`() = runBlockingTest {

        val result = characterRepository.getCharacter(1, 1, "hash")

        assertTrue(result is Outcome.Success)
        val characterResult = assertNotNull((result as Outcome.Success).value)
        assertEquals(character, characterResult)
    }

    @Test
    fun `getCharacter with null body returns null`() = runBlockingTest {
        every { characterResponse.body() } returns null

        val result = characterRepository.getCharacter(1, 1, "hash")

        assertTrue(result is Outcome.Success)
        assertNull((result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacter(1, 1, "hash") }
        verify { characterResponse.body() }
        verify(exactly = 0) { marvelDataWrapper.data }
    }

    @Test
    fun `getCharacter with null data returns null`() = runBlockingTest {
        every { marvelDataWrapper.data } returns null

        val result = characterRepository.getCharacter(1, 1, "hash")

        assertTrue(result is Outcome.Success)
        assertNull((result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacter(1, 1, "hash") }
        verify { characterResponse.body() }
        verify { marvelDataWrapper.data }
        verify(exactly = 0) { dataNetworkDto.results }
    }

    @Test
    fun `getCharacter with null results returns null`() = runBlockingTest {
        every { dataNetworkDto.results } returns null

        val result = characterRepository.getCharacter(1, 1, "hash")

        assertTrue(result is Outcome.Success)
        assertNull((result as Outcome.Success).value)
        coVerify { characterNetworkService.getCharacter(1, 1, "hash") }
        verify { characterResponse.body() }
        verify { marvelDataWrapper.data }
        verify { dataNetworkDto.results }
    }

    @Test
    fun `getCharacter returns outcome error`() = runBlockingTest {
        every { characterResponse.isSuccessful } returns false
        every { characterResponse.code() } returns 404
        every { characterResponse.message() } returns "errorMessage"

        val result = characterRepository.getCharacter(1, 1, "hash")

        assertTrue(result is Outcome.Error)
        val details = assertNotNull((result as Outcome.Error).exceptionDetails)
        assertEquals(404, details.code)
        assertEquals("errorMessage", details.cause)
    }

    private fun mockResponse(marvelDataWrapper: MarvelDataWrapper<CharacterNetworkDto>): Response<MarvelDataWrapper<CharacterNetworkDto>> {
        val response: Response<MarvelDataWrapper<CharacterNetworkDto>> = mockk {
            every {
                hint(MarvelDataWrapper::class)
                body()
            } returns marvelDataWrapper
            every { isSuccessful } returns true
        }
        return response
    }

    private fun mockDataWrapper(dataNetworkDto: DataNetworkDto<CharacterNetworkDto>): MarvelDataWrapper<CharacterNetworkDto> {
        val marvelDataWrapper: MarvelDataWrapper<CharacterNetworkDto> = mockk {
            every { data } returns dataNetworkDto
        }
        return marvelDataWrapper
    }

    private fun mockDataNetworkDto(characterNetworkDtoList: List<CharacterNetworkDto>): DataNetworkDto<CharacterNetworkDto> {
        val dataNetworkDto: DataNetworkDto<CharacterNetworkDto> = mockk {
            every { results } returns characterNetworkDtoList
        }
        return dataNetworkDto
    }

    private fun mockCharacterNetworkDto(image: ImageNetworkDto): CharacterNetworkDto {
        val characterNetworkDto: CharacterNetworkDto = mockk {
            every { id } returns 1
            every { name } returns "name"
            every { description } returns "description"
            every { thumbnail } returns image
        }
        return characterNetworkDto
    }

    private fun mockImageNetworkDto(): ImageNetworkDto {
        val image: ImageNetworkDto = mockk {
            every { path } returns "path"
            every { extension } returns "jpg"
        }
        return image
    }
}