package org.example.service

import kotlinx.serialization.json.Json
import model.Quote
import model.QuoteResponse
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object QuoteServiceHttp {

    private val baseUrl: String = QuoteServiceHttp.getProperties("quote_base_url") ?: error("base_url ausente")
    private val basePath: String = QuoteServiceHttp.getProperties("quote_base_path") ?: error("base_path ausente")
    private val json = Json { ignoreUnknownKeys = true }

    fun fetchQuoteInfo(quoteId: Int): String {
        val urlString = "https://$baseUrl$basePath/" + quoteId.toString()
        val connection = URL(urlString).openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()
        return response
    }

    fun fetchAllQuotes(): List<Quote> {
        val urlString = "https://$baseUrl$basePath"
        val connection = URL(urlString).openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        val quoteResponse: QuoteResponse = json.decodeFromString(response)
        return quoteResponse.quotes
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
