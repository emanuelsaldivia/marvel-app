package com.esaldivia.marvelheroes.usecase

import com.esaldivia.marvelheroes.BaseTest
import com.esaldivia.marvelheroes.data.model.ImageNetworkDto
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ImageUseCaseTest : BaseTest() {
    private lateinit var imageUseCase: ImageUseCase
    private val path = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784"
    private val extension = "jgp"

    @Before
    fun setUp() {
        imageUseCase = ImageUseCase()
    }

    @Test
    fun `getSmallPortraitImage returns correct paths`() {
        val image = ImageNetworkDto(path, extension)

        val result = imageUseCase.getSmallPortraitImageUri(image)

        val expected = path + "/" + ImageUseCase.LANDSCAPE_XL + "." + extension
        assertEquals(expected, result)
    }
}