package com.esaldivia.marvelheroes.network

import com.esaldivia.marvelheroes.data.model.character.CharacterNetworkDto
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CharacterNetworkServiceTest : BaseNetworkServiceTest() {
    private lateinit var characterNetworkService: CharacterNetworkService

    @Before
    fun setUp() {
        characterNetworkService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterNetworkService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun `getCharacters returns correct MarvelWrapper`() = runBlocking {
        enqueueResponse("/CharactersResponse.json")

        var result = characterNetworkService.getCharacters(hash = "hash").body()

        result = assertNotNull(result)
        assertEquals(200, result.code)
        assertEquals("Ok", result.status)
        assertEquals("© 2022 MARVEL", result.copyright)
        assertEquals("Data provided by Marvel. © 2022 MARVEL", result.attributionText)
        assertEquals("acf9f51e34e86527b1731e31d1d82ffa30d9cb46", result.eTag)
    }

    @Throws(IOException::class)
    @Test
    fun `getCharacters returns correct dataNetworkDto`() = runBlocking {
        enqueueResponse("/CharactersResponse.json")

        val result = characterNetworkService.getCharacters(hash = "hash").body()

        val data = assertNotNull(result?.data)
        assertEquals(0, data.offset)
        assertEquals(20, data.limit)
        assertEquals(1559, data.total)
        val results = assertNotNull(data.results)
        assertEquals(data.count, results.size)
    }

    @Throws(IOException::class)
    @Test
    fun `getCharacters returns correct characters`() = runBlocking {
        enqueueResponse("/CharactersResponse.json")

        val result = characterNetworkService.getCharacters(hash = "hash").body()

        val character = assertNotNull(result?.data?.results?.get(0))
        assertEquals(1011334, character.id)
        assertEquals("3-D Man", character.name)
        assertEquals("", character.description)
        val thumbnail = assertNotNull(character.thumbnail)
        assertEquals("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784", thumbnail.path)
        assertEquals("jpg", thumbnail.extension)
        assertEquals(
            "http://gateway.marvel.com/v1/public/characters/1011334",
            character.resourceUri
        )
        assertComics(character)
        assertSeries(character)
        assertStories(character)
        assertEvents(character)
    }

    @Throws(IOException::class)
    @Test
    fun `getCharacter returns correct MarvelWrapper`() = runBlocking {
        enqueueResponse("/CharacterResponse.json")

        var result = characterNetworkService.getCharacters(hash = "hash").body()

        result = assertNotNull(result)
        assertEquals(200, result.code)
        assertEquals("Ok", result.status)
        assertEquals("© 2022 MARVEL", result.copyright)
        assertEquals("Data provided by Marvel. © 2022 MARVEL", result.attributionText)
        assertEquals("55342c8b21941bfea4b795ff85633d9063e1da0e", result.eTag)
    }

    @Throws(IOException::class)
    @Test
    fun `getCharacter returns correct dataNetworkDto`() = runBlocking {
        enqueueResponse("/CharacterResponse.json")

        val result = characterNetworkService.getCharacters(hash = "hash").body()

        val data = assertNotNull(result?.data)
        assertEquals(0, data.offset)
        assertEquals(20, data.limit)
        assertEquals(1, data.total)
        val results = assertNotNull(data.results)
        assertEquals(data.count, results.size)
    }

    @Throws(IOException::class)
    @Test
    fun `getCharacter returns correct Character`() = runBlocking {
        enqueueResponse("/CharacterResponse.json")

        val result = characterNetworkService.getCharacter(1011334, 1, "hash").body()

        val character = assertNotNull(result?.data?.results?.get(0))
        assertEquals(1011334, character.id)
        assertEquals("3-D Man", character.name)
        assertEquals("", character.description)
        val thumbnail = assertNotNull(character.thumbnail)
        assertEquals("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784", thumbnail.path)
        assertEquals("jpg", thumbnail.extension)
        assertEquals(
            "http://gateway.marvel.com/v1/public/characters/1011334",
            character.resourceUri
        )
        assertComics(character)
        assertSeries(character)
        assertStories(character)
        assertEvents(character)
    }

    private fun assertComics(character: CharacterNetworkDto) {
        val comics = assertNotNull(character.comics)
        assertEquals(12, comics.available)
        assertEquals(comics.returned, comics.items?.size)
        assertEquals(
            "http://gateway.marvel.com/v1/public/characters/1011334/comics",
            comics.collectionUri
        )
        val firstComic = assertNotNull(comics.items?.get(0))
        assertEquals("http://gateway.marvel.com/v1/public/comics/21366", firstComic.resourceUri)
        assertEquals("Avengers: The Initiative (2007) #14", firstComic.name)
    }

    private fun assertSeries(character: CharacterNetworkDto) {
        val series = assertNotNull(character.series)
        assertEquals(3, series.available)
        assertEquals(series.returned, series.items?.size)
        assertEquals(
            "http://gateway.marvel.com/v1/public/characters/1011334/series",
            series.collectionUri
        )
        val firstSeries = assertNotNull(series.items?.get(0))
        assertEquals("http://gateway.marvel.com/v1/public/series/1945", firstSeries.resourceUri)
        assertEquals("Avengers: The Initiative (2007 - 2010)", firstSeries.name)
    }

    private fun assertStories(character: CharacterNetworkDto) {
        val stories = assertNotNull(character.stories)
        assertEquals(21, stories.available)
        assertEquals(stories.returned, stories.items?.size)
        assertEquals(
            "http://gateway.marvel.com/v1/public/characters/1011334/stories",
            stories.collectionUri
        )
        val firstStory = assertNotNull(character.stories?.items?.get(0))
        assertEquals("http://gateway.marvel.com/v1/public/stories/19947", firstStory.resourceUri)
        assertEquals("Cover #19947", firstStory.name)
        assertEquals("cover", firstStory.type)
    }

    private fun assertEvents(character: CharacterNetworkDto) {
        val events = assertNotNull(character.events)
        assertEquals(1, events.available)
        assertEquals(events.returned, events.items?.size)
        assertEquals(
            "http://gateway.marvel.com/v1/public/characters/1011334/events",
            events.collectionUri
        )
        val firstEvent = assertNotNull(events.items?.get(0))
        assertEquals("http://gateway.marvel.com/v1/public/events/269", firstEvent.resourceUri)
        assertEquals("Secret Invasion", firstEvent.name)
    }
}
