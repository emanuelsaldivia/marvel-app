package com.esaldivia.marvelheroes.ui.character

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.esaldivia.marvelheroes.BaseTest
import com.esaldivia.marvelheroes.common.Resource
import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.data.repository.CharacterRepository
import com.esaldivia.marvelheroes.di.TestDispatchersModule
import com.esaldivia.marvelheroes.network.NetworkCallException
import com.esaldivia.marvelheroes.network.Outcome
import com.esaldivia.marvelheroes.openMarvelScope
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CharactersViewModelTest : BaseTest() {
    private lateinit var charactersViewModel: CharactersViewModel
    private lateinit var characterRepository: CharacterRepository
    private lateinit var listObserver: Observer<Resource<List<Character>?>>
    private lateinit var characterObserver: Observer<Resource<Character?>>
    private val character = Character(1, "name", "description", "path")
    private val characterList: List<Character> = listOf(character)

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        characterRepository = mockk()
        listObserver = mockk(relaxed = true)
        characterObserver = mockk(relaxed = true)

        KTP.openMarvelScope()
            .installTestModules(TestDispatchersModule(),
                module {
                    bind<CharacterRepository>().toInstance(characterRepository)
                })
            .inject(this)

        charactersViewModel = CharactersViewModel()
    }

    @Test
    fun `getCharacterList return Resource success`() {
        val outcome: Outcome.Success<List<Character>?> = mockk {
            every { value } returns characterList
        }
        coEvery { characterRepository.getCharacters(any(), any()) } returns outcome
        charactersViewModel.charactersListLiveData.observeForever(listObserver)

        charactersViewModel.getCharacterList()

        verify {
            listObserver.onChanged(withArg {
                assertTrue(it is Resource.Success)
                it as Resource.Success
                assertEquals(characterList, it.value)

            })
        }
    }

    @Test
    fun `getCharacterList return Resource loading`() {
        val outcome: Outcome.Success<List<Character>?> = mockk {
            every { value } returns characterList
        }
        coEvery { characterRepository.getCharacters(any(), any()) } returns outcome
        charactersViewModel.charactersListLiveData.observeForever(listObserver)

        charactersViewModel.getCharacterList()

        verify {
            listObserver.onChanged(withArg {
                assertTrue(it is Resource.Loading)
            })
        }
    }

    @Test
    fun `getCharacterList return Resource error`() {
        val expectedMessage = "error message"
        val outcome: Outcome.Error<List<Character>?> = mockk {
            every { exceptionDetails } returns NetworkCallException(400, expectedMessage)
        }
        coEvery { characterRepository.getCharacters(any(), any()) } returns outcome
        charactersViewModel.charactersListLiveData.observeForever(listObserver)

        charactersViewModel.getCharacterList()

        verify {
            listObserver.onChanged(withArg {
                assertTrue(it is Resource.Error)
                it as Resource.Error
                assertEquals(expectedMessage, it.errorMessage)
            })
        }
    }

    @Test
    fun `getCharacter return Resource success`() {
        val outcome: Outcome.Success<Character?> = mockk {
            every { value } returns character
        }
        coEvery { characterRepository.getCharacter(any(), any()) } returns outcome
        charactersViewModel.characterLiveData.observeForever(characterObserver)

        charactersViewModel.getCharacter(1)

        verify {
            characterObserver.onChanged(withArg {
                assertTrue(it is Resource.Success)
                it as Resource.Success
                assertEquals(character, it.value)
            })
        }
    }

    @Test
    fun `getCharacter return Resource loading`() {
        val outcome: Outcome.Success<Character?> = mockk {
            every { value } returns character
        }
        coEvery { characterRepository.getCharacter(any(), any()) } returns outcome
        charactersViewModel.characterLiveData.observeForever(characterObserver)

        charactersViewModel.getCharacter(1)

        verify {
            characterObserver.onChanged(withArg {
                assertTrue(it is Resource.Loading)
            })
        }
    }

    @Test
    fun `getCharacter return Resource error`() {
        val expectedMessage = "error message"
        val outcome: Outcome.Error<Character?> = mockk {
            every { exceptionDetails } returns NetworkCallException(400, expectedMessage)
        }
        coEvery { characterRepository.getCharacter(any(), any()) } returns outcome
        charactersViewModel.characterLiveData.observeForever(characterObserver)

        charactersViewModel.getCharacter(1)

        verify {
            characterObserver.onChanged(withArg {
                assertTrue(it is Resource.Error)
                it as Resource.Error
                assertEquals(expectedMessage, it.errorMessage)
            })
        }
    }
}