package com.esaldivia.marvelheroes.usecase

import com.esaldivia.marvelheroes.data.model.ImageNetworkDto
import javax.inject.Inject

class ImageUseCase @Inject constructor() {

    fun getSmallPortraitImageUri(image: ImageNetworkDto): String {
        return image.path + "/" + LANDSCAPE_XL + "." + image.extension
    }

    companion object {
        const val LANDSCAPE_XL = "landscape_xlarge"
    }
}

