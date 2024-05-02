package com.example.navigationapp.data.remote.responses

data class Maneuver(
    val bearing_after: Int,
    val bearing_before: Int,
    val instruction: String,
    val location: List<Double>,
    val modifier: String,
    val type: String
)