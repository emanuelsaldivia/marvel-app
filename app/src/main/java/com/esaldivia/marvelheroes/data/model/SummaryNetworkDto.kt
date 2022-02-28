package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class SummaryNetworkDto (
    @SerializedName("resourceURI")
    val resourceUri: String? = null,
    @SerializedName("name")
    val name: String? = null)