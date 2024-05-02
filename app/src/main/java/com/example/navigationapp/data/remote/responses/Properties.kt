package com.example.navigationapp.data.remote.responses

data class Properties(
    val address: String,
    val context: ContextX,
    val coordinates: Coordinates,
    val feature_type: String,
    val full_address: String,
    val language: String,
    val maki: String,
    val mapbox_id: String,
    val metadata: MetadataX,
    val name: String,
    val name_preferred: String,
    val place_formatted: String
)