package com.esaldivia.marvelheroes.data.repository

import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.network.CharacterNetworkService
import com.esaldivia.marvelheroes.network.Outcome
import com.esaldivia.marvelheroes.usecase.ImageUseCase
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterNetwork: CharacterNetworkService,
    private val imageUseCase: ImageUseCase
) : NetworkRepository() {

    suspend fun getCharacters(
        timeStamp: Long = 1,
        offset: Int = 0,
        hash: String
    ): Outcome<List<Character>?> {
        val outcome = runNetworkCall {
            characterNetwork.getCharacters(timeStamp, offset, hash)
        }

        return when (outcome) {
            is Outcome.Success -> {
                val characterList = outcome.value?.data?.results?.map {
                    Character(
                        it.id,
                        it.name,
                        it.description,
                        it.thumbnail?.let { image -> imageUseCase.getLargeLandscapeImageUri(image) }
                    )
                }

                Outcome.Success(characterList)
            }
            is Outcome.Error -> {
                Outcome.Error(outcome.errorCode, outcome.exception, outcome.exceptionDetails)
            }
        }
    }

    suspend fun getCharacter(
        characterId: Int,
        timeStamp: Long = 1,
        hash: String
    ): Outcome<Character?> {
        val outcome = runNetworkCall {
            characterNetwork.getCharacter(characterId, timeStamp, hash)
        }

        return when (outcome) {
            is Outcome.Success -> {
                val characterNetworkDto = outcome.value?.data?.results?.get(0) ?: return Outcome.Success(null)
                val character = Character(
                    characterNetworkDto.id,
                    characterNetworkDto.name,
                    characterNetworkDto.description,
                    characterNetworkDto.thumbnail?.let { image ->
                        imageUseCase.getLargeLandscapeImageUri(image)
                    })
                Outcome.Success(character)
            }
            is Outcome.Error -> {
                Outcome.Error(outcome.errorCode, outcome.exception, outcome.exceptionDetails)
            }
        }
    }
}