package org.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,

    @SerialName("current_price")
    val currentPrice: Double,

    @SerialName("market_cap")
    val marketCap: Long,

    @SerialName("market_cap_rank")
    val marketCapRank: Int,

    @SerialName("total_volume")
    val totalVolume: Double,

    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,

    @SerialName("circulating_supply")
    val circulatingSupply: Double,

    @SerialName("total_supply")
    val totalSupply: Double? = null,

    @SerialName("max_supply")
    val maxSupply: Double? = null,

    @SerialName("ath")
    val ath: Double,

    @SerialName("ath_change_percentage")
    val athChangePercentage: Double,

    @SerialName("atl")
    val atl: Double,

    @SerialName("atl_change_percentage")
    val atlChangePercentage: Double,

    @SerialName("last_updated")
    val lastUpdated: String
)