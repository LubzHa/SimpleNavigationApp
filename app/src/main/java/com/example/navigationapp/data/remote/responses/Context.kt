package com.example.navigationapp.data.remote.responses

data class Context(
    val address: Address,
    val country: Country,
    val district: District,
    val place: Place,
    val postcode: Postcode,
    val region: Region,
    val street: Street
)