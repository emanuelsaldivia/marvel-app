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
        timeStamp: Int = 1,
        offset: Int = 0
    ): Outcome<List<Character>?> {
        val outcome = runNetworkCall {
            characterNetwork.getCharactersFromApi(timeStamp, offset)
        }

        return when (outcome) {
            is Outcome.Success -> {
                val characterList = outcome.value?.data?.results?.map {
                    Character(
                        it.id,
                        it.name,
                        it.description,
                        it.thumbnail?.let { image -> imageUseCase.getSmallPortraitImageUri(image) }
                    )
                }

                Outcome.Success(characterList)
            }
            is Outcome.Error -> {
                Outcome.Error(outcome.errorCode, outcome.exception, outcome.exceptionDetails)
            }
        }
    }
}