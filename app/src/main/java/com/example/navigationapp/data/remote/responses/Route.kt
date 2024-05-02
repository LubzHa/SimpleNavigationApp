package com.example.navigationapp.data.remote.responses

data class Route(
    val distance: Double,
    val duration: Double,
    val geometry: Geometry,
    val legs: List<Leg>,
    val weight: Double,
    val weight_name: String
)