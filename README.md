# Simple Navigation Application

This is an application made in the context of a technical test.

This app allows the user to enter a departure and an arrival addresses, and to visualise the route between the two.
It uses the Mapbox SDK for map displaying and route visualisation, the Mapbox Search API for the address search and the Mapbox Directions API to compute the route between the two points.
The UI is made with Jetpack Compose, and the Mapbox SDK Compose extension.
It also uses Dagger-Hilt for dependency injection, and Retrofit for API calls.

The application follows the MVVM design pattern.

## Possible further improvements

* Display the route steps as a list (maybe as a bottom sheet like for the addresses suggestions)
* Better zoom management. For example when the two points are entered, adjust the camera to fit both points in the view.
* Better Route display. Simple blue lines could be improved.
