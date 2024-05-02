package com.example.navigationapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationapp.data.models.AddressSuggestionEntry
import com.example.navigationapp.data.repository.DirectionsRepository
import com.example.navigationapp.util.Event
import com.example.navigationapp.util.Resource
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchField {
    DEPARTURE, ARRIVAL
}

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val repository: DirectionsRepository
) : ViewModel() {

    var statusMessage = MutableLiveData<Event<String>>()
        private set

    val viewPortState = MapViewportState().apply {
        setCameraOptions {
            zoom(11.0)
            center(Point.fromLngLat(2.333333, 48.866667))
            pitch(0.0)
            bearing(0.0)
        }
    }

    var startAddress = mutableStateOf("")
        private set
    var endAddress = mutableStateOf("")
        private set
    var startPointCoordinates by mutableStateOf<Point?>(null)
        private set
    var endPointCoordinates by mutableStateOf<Point?>(null)
        private set
    var coordinatesChanged by mutableStateOf(false)
        private set

    var route = mutableStateListOf<Point>()
        private set
    var routeChanged by mutableStateOf(false)
        private set

    var suggestedAddresses = mutableStateListOf<AddressSuggestionEntry>()
        private set

    var currentSearchField by mutableStateOf(SearchField.DEPARTURE)

    fun setPointTo(address: AddressSuggestionEntry) {
        viewModelScope.launch {
            route.clear()
            when(currentSearchField) {
                SearchField.DEPARTURE -> {
                    startAddress.value = address.name
                    startPointCoordinates = retrieveAddressCoordinates(address.mapboxId).single()
                    startPointCoordinates?.let {
                        updateMapViewPort(it)
                    }
                }

                SearchField.ARRIVAL -> {
                    endAddress.value = address.name
                    endPointCoordinates = retrieveAddressCoordinates(address.mapboxId).single()
                    endPointCoordinates?.let {
                        updateMapViewPort(it)
                    }
                }
            }
            routeChanged = !routeChanged
        }
    }

    private fun retrieveAddressCoordinates(mapboxId: String): Flow<Point?> =
        flow {
            val result = repository.getAddressCoordinates(mapboxId)
            when (result) {
                is Resource.Success -> {
                    val coordinates = result.data!!.features.first().properties.coordinates
                    coordinatesChanged = true
                    emit(Point.fromLngLat(coordinates.longitude, coordinates.latitude))
                }
                is Resource.Error -> {
                    println(result.message!!)
                    emit(null)
                }
            }
        }.flowOn(Dispatchers.Default)


    fun searchForAddressSuggestions(searchAddress: String) {
        viewModelScope.launch {
            val searchSuggestions = repository.getAddressSearchResults(searchAddress)
            when (searchSuggestions) {
                is Resource.Success -> {
                    suggestedAddresses.clear()
                    suggestedAddresses.addAll(searchSuggestions.data!!)
                }
                is Resource.Error -> {
                    statusMessage.value = Event(searchSuggestions.message!!)
                }
            }
        }
    }


    fun searchForRoute() {
        viewModelScope.launch {
            if (startPointCoordinates == null || endPointCoordinates == null) {
                return@launch
            }
            val result = repository.getWalkingDirections(startPointCoordinates!!, endPointCoordinates!!)
            when(result) {
                is Resource.Success -> {
                    route.clear()
                    route.addAll(result.data!!)
                    routeChanged = !routeChanged
                }
                is Resource.Error -> {
                    statusMessage.value = Event(result.message!!)
                }
            }
        }
    }

    private fun updateMapViewPort(center: Point) {
        // TODO: Calculate the zoom level that fits both points
        viewPortState.flyTo(
            CameraOptions.Builder()
                .center(center)
//                .zoom(11.0)
                .bearing(0.0)
                .pitch(0.0)
                .build()
        )
    }

}

