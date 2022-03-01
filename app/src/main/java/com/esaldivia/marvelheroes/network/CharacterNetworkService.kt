package com.esaldivia.marvelheroes.network

import com.esaldivia.marvelheroes.data.model.MarvelDataWrapper
import com.esaldivia.marvelheroes.data.model.character.CharacterNetworkDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterNetworkService {

    @GET("/v1/public/characters")
    suspend fun getCharactersFromApi(
        @Query("ts") timeStamp: Int = 1,
        @Query("offset") offset: Int = 0
    ): Response<MarvelDataWrapper<CharacterNetworkDto>>
}