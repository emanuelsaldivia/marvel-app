package com.esaldivia.marvelheroes.data.model.character

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val id: Int? = 0,
    val name: String? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null) : Parcelable
