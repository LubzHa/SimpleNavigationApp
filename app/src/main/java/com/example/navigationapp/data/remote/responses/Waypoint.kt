package com.example.navigationapp.data.remote.responses

data class Waypoint(
    val distance: Double,
    val location: List<Double>,
    val name: String
)