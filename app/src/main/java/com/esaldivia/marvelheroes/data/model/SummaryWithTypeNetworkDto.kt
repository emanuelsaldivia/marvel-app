package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class SummaryWithTypeNetworkDto (
    @SerializedName("resourceURI")
    val resourceUri: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("type")
    val type: String? = null)
