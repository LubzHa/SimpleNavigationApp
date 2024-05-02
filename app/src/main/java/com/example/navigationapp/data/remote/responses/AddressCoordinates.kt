package com.example.navigationapp.data.remote.responses

data class AddressCoordinates(
    val attribution: String,
    val features: List<Feature>,
    val type: String,
    val url: String
)