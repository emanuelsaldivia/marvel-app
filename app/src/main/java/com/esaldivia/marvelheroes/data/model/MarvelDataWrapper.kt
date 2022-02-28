package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class MarvelDataWrapper<T>(
    @SerializedName("code")
    val code: Int? = 0,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("copyright")
    val copyright: String? = null,
    @SerializedName("attributionText")
    val attributionText: String? = null,
    @SerializedName("attributionHTML")
    val attributionHtml: String? = null,
    @SerializedName("etag")
    val eTag: String? = null,
    @SerializedName("data")
    val data: DataNetworkDto<T>? = null
)