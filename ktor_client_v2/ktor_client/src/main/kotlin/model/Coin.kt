package org.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
)