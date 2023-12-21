package com.example.willog_unsplash.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photos(
    @field:Json(name = "total")
    val total: Int,
    @field:Json(name = "total_pages")
    val total_pages: Int,
    @field:Json(name = "results")
    val results: List<PhotoData>
)
