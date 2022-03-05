package com.esaldivia.marvelheroes.data.model.character

import com.esaldivia.marvelheroes.data.model.*
import com.esaldivia.marvelheroes.data.model.SummaryNetworkDto
import com.google.gson.annotations.SerializedName
import java.util.*

data class CharacterNetworkDto(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("modified")
    val modified: Date? = null,
    @SerializedName("resourceURI")
    val resourceUri: String? = null,
    @SerializedName("urls")
    val urls: List<UrlNetworkDto>? = null,
    @SerializedName("thumbnail")
    val thumbnail: ImageNetworkDto? = null,
    @SerializedName("comics")
    val comics: ItemMetadataNetworkDto<SummaryNetworkDto>? = null,
    @SerializedName("stories")
    val stories: ItemMetadataNetworkDto<SummaryWithTypeNetworkDto>? = null,
    @SerializedName("events")
    val events: ItemMetadataNetworkDto<SummaryNetworkDto>? = null,
    @SerializedName("series")
    val series: ItemMetadataNetworkDto<SummaryNetworkDto>? = null
)
