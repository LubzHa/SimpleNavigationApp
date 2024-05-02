package com.example.navigationapp.data.remote.responses

data class AddressSearchSuggestions(
    val attribution: String,
    val suggestions: List<Suggestion>,
    val url: String
)