package com.example.navigationapp.data.remote.responses

data class Step(
    val distance: Double,
    val driving_side: String,
    val duration: Double,
    val geometry: Geometry,
    val intersections: List<Intersection>,
    val maneuver: Maneuver,
    val mode: String,
    val name: String,
    val weight: Double
)