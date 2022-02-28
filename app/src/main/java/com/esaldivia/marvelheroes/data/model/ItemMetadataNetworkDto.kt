package com.esaldivia.marvelheroes.data.model

import com.google.gson.annotations.SerializedName

data class ItemMetadataNetworkDto<T>(
    @SerializedName("available")
    val available: Int? = 0,
    @SerializedName("returned")
    val returned: Int? = 0,
    @SerializedName("collectionURI")
    val collectionUri: String? = null,
    @SerializedName("items")
    val items: List<T>? = null
)
