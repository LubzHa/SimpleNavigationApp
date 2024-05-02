package com.example.navigationapp.data.repository

import com.example.navigationapp.data.models.AddressSuggestionEntry
import com.example.navigationapp.data.remote.DirectionsAPI
import com.example.navigationapp.data.remote.responses.AddressCoordinates
import com.example.navigationapp.data.remote.responses.AddressSearchSuggestions
import com.example.navigationapp.data.remote.responses.Directions
import com.example.navigationapp.util.Resource
import com.mapbox.geojson.Point
import dagger.hilt.android.scopes.ActivityScoped
import java.util.UUID
import javax.inject.Inject

@ActivityScoped
class DirectionsRepository @Inject constructor(
    private val api: DirectionsAPI
) {

    private val SESSION_TOKEN = UUID.randomUUID().toString()

    suspend fun getWalkingDirections(startCoordinates: Point, endCoordinates: Point): Resource<List<Point>> {
        val response = try {
            val startCoordinatesString = "${startCoordinates.longitude()},${startCoordinates.latitude()}"
            val endCoordinatesString = "${endCoordinates.longitude()},${endCoordinates.latitude()}"
            api.getWalkingDirections(startCoordinatesString, endCoordinatesString)
        } catch (e: Exception) {
            return Resource.Error("Unable to retrieve directions")
        }
        return Resource.Success(response.routes.first().geometry.coordinates.map {
            Point.fromLngLat(it.first(), it.last())
        }.toList())
    }

    suspend fun getAddressSearchResults(searchQuery: String): Resource<List<AddressSuggestionEntry>> {
        val response = try {
            api.getAddressSearchResults(searchQuery, sessionToken = SESSION_TOKEN)
        } catch (e: Exception) {
            return Resource.Error("Unable to retrieve search results. Message : ${e.message}")
        }
        return Resource.Success(response.suggestions.map {
            AddressSuggestionEntry(it.name, it.full_address, it.mapbox_id)
        }.toList())
    }

    suspend fun getAddressCoordinates(mapboxId: String): Resource<AddressCoordinates> {
        val response = try {
            api.getAddressCoordinates(mapboxId, sessionToken = SESSION_TOKEN)
        } catch (e: Exception) {
            return Resource.Error("Unable to retrieve address coordinates. Message : ${e.message}")
        }
        return Resource.Success(response)
    }
}