package com.example.navigationapp.data.models

import com.example.navigationapp.data.remote.responses.AddressCoordinates
import com.mapbox.geojson.Point

data class AddressSuggestionEntry(
    val name: String,
    val address: String?,
    val mapboxId: String
)
