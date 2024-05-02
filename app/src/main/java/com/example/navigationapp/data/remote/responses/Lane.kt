package com.example.navigationapp.data.remote.responses

data class Lane(
    val active: Boolean,
    val indications: List<String>,
    val valid: Boolean,
    val valid_indication: String
)