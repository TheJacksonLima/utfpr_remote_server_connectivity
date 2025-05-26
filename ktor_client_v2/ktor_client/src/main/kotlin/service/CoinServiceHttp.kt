package org.example.service


import kotlinx.serialization.json.Json
import org.example.model.Coin
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object CoinServiceHttp {

    private val apiKey: String = System.getenv("API_KEY") ?: error("API_KEY não configurada")
    private val baseUrl: String = getProperties("coin_gecko_base_url") ?: error("base_url ausente")
    private val basePath: String = getProperties("coin_gecko_base_path") ?: error("base_path ausente")
    private val json = Json { ignoreUnknownKeys = true }

    fun fetchCoinInfo(coinId: String): String {
        val urlString = "https://$baseUrl$basePath$coinId?vs_currency=usd&x_cg_demo_api_key=$apiKey"
        val connection = URL(urlString).openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()
        return response
    }

    fun fetchAllCoins(): List<Coin> {
        val urlString = "https://$baseUrl${basePath}list?vs_currency=usd&x_cg_demo_api_key=$apiKey"
        val connection = URL(urlString).openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        return json.decodeFromString(response)
    }

    private fun getProperties(pProperty: String): String? {
        val properties = Properties()
        try {
            val inputStream = ClassLoader.getSystemResourceAsStream("application.properties")
                ?: FileInputStream("application.properties")

            properties.load(inputStream)
            inputStream.close()
            return properties.getProperty(pProperty)
        } catch (e: IOException) {
            System.err.println("Erro ao carregar propriedades: ${e.message}")
            return null
        }
    }
}
