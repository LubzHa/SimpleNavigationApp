package com.example.navigationapp.data.remote.responses

data class Directions(
    val code: String,
    val routes: List<Route>,
    val uuid: String,
    val waypoints: List<Waypoint>
)