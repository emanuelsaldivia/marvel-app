package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class ItemNetworkDto (
    @SerializedName("resourceUri")
    val resourceUri: String? = null,
    @SerializedName("name")
    val name: String? = null
)