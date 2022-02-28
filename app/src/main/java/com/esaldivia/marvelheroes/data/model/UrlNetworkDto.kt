package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class UrlNetworkDto(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("url")
    val url: String? = null
)
