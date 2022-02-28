package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class ImageNetworkDto(
    @SerializedName("path")
    val path: String? = null,
    @SerializedName("extension")
    val extension: String? = null
)
