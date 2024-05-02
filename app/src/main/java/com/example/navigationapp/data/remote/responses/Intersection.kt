package com.example.navigationapp.data.remote.responses

data class Intersection(
    val admin_index: Int,
    val bearings: List<Int>,
    val duration: Double,
    val entry: List<Boolean>,
    val geometry_index: Int,
    val `in`: Int,
    val is_urban: Boolean,
    val lanes: List<Lane>,
    val location: List<Double>,
    val mapbox_streets_v8: MapboxStreetsV8,
    val `out`: Int,
    val traffic_signal: Boolean,
    val turn_duration: Int,
    val turn_weight: Int,
    val weight: Double
)