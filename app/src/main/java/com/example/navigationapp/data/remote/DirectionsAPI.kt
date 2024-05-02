package com.example.navigationapp.data.remote

import com.example.navigationapp.R
import com.example.navigationapp.data.remote.responses.AddressCoordinates
import com.example.navigationapp.data.remote.responses.AddressSearchSuggestions
import com.example.navigationapp.data.remote.responses.Directions
import com.example.navigationapp.util.Constants.MAPBOX_PUBLIC_TOKEN
import com.mapbox.geojson.Point
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DirectionsAPI {
    @GET("directions/v5/mapbox/walking/{start_coordinates};{end_coordinates}")
    suspend fun getWalkingDirections(
        @Path("start_coordinates") startCoordinates: String,
        @Path("end_coordinates") endCoordinates: String,
        @Query("access_token") accessToken: String =  MAPBOX_PUBLIC_TOKEN,
        @Query("alternatives") alternative: Boolean = false,
        @Query("geometries") geometries: String = "geojson"
    ): Directions

    @GET("search/searchbox/v1/suggest")
    suspend fun getAddressSearchResults(
        @Query("q") searchQuery: String,
        @Query("navigation_profile") navigationProfile: String = "walking",
        @Query("access_token") accessToken: String = MAPBOX_PUBLIC_TOKEN,
        @Query("session_token") sessionToken: String,
    ): AddressSearchSuggestions

    @GET("search/searchbox/v1/retrieve/{mapbox_id}")
    suspend fun getAddressCoordinates(
        @Path("mapbox_id") mapboxId: String,
        @Query("access_token") accessToken: String = MAPBOX_PUBLIC_TOKEN,
        @Query("session_token") sessionToken: String,
    ): AddressCoordinates
}