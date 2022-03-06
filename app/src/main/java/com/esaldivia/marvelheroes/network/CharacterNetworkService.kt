package com.esaldivia.marvelheroes.network

import com.esaldivia.marvelheroes.data.model.MarvelDataWrapper
import com.esaldivia.marvelheroes.data.model.character.CharacterNetworkDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterNetworkService {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("ts") timeStamp: Long = 1,
        @Query("offset") offset: Int = 0,
        @Query("hash") hash: String
    ): Response<MarvelDataWrapper<CharacterNetworkDto>>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacter(
        @Path("characterId") characterId: Int,
        @Query("ts") timeStamp: Long = 1,
        @Query("hash") hash: String
    ): Response<MarvelDataWrapper<CharacterNetworkDto>>
}