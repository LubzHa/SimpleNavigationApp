package com.example.navigationapp.data.remote.responses

data class Suggestion(
    val address: String?,
    val brand: List<String>,
    val brand_id: List<String>,
    val context: Context,
    val external_ids: ExternalIds,
    val feature_type: String,
    val full_address: String?,
    val language: String,
    val maki: String,
    val mapbox_id: String,
    val metadata: Metadata,
    val name: String,
    val name_preferred: String?,
    val place_formatted: String?,
    val poi_category: List<String>?,
    val poi_category_ids: List<String>?
)