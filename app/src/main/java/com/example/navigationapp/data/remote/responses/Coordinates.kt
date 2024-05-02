package com.example.navigationapp.data.remote.responses

data class Coordinates(
    val accuracy: String,
    val latitude: Double,
    val longitude: Double,
    val routable_points: List<RoutablePoint>
)