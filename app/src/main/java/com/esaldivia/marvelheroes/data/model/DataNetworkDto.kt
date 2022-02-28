package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class DataNetworkDto<T>(
    @SerializedName("offset")
    val offset: Int? = 0,
    @SerializedName("limit")
    val limit: Int? = 0,
    @SerializedName("total")
    val total: Int? = 0,
    @SerializedName("count")
    val count: Int? = 0,
    @SerializedName("results")
    val results: List<T>? = null
)
