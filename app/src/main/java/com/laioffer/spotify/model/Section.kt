package com.laioffer.spotify.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Section(
    @SerializedName("section_title")
    val sectionTitle: String,
    val albums: List<Album>
) : Serializable
