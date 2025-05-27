package org.example.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.*
import model.Quote
import model.QuoteResponse
import java.io.FileInputStream
import java.io.IOException
import java.util.*

object QuoteServiceKtor {
    private val baseUrl: String = QuoteServiceKtor.getProperties("quote_base_url") ?: error("base_url ausente")
    private val basePath: String = QuoteServiceKtor.getProperties("quote_base_path") ?: error("base_path ausente")

    private val client = HttpClient(CIO) {
        engine {
            maxConnectionsCount = 1000
            endpoint {
                connectTimeout = 5_000
                requestTimeout = 10_000
                keepAliveTime = 5000
                pipelineMaxSize = 20
                maxConnectionsPerRoute = 100
            }
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchAllQuotes(): List<Quote> {
        val response: QuoteResponse = client.get {
            url("https://$baseUrl$basePath")
        }.body()
        return response.quotes
    }

    suspend fun fetchQuoteInfo(quoteId : Int): Quote {
        val response: Quote = client.get {
            url("https://$baseUrl$basePath/$quoteId")
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
        }.body()

        return response
    }

    private fun getProperties(pProperty: String): String? {
        val properties = Properties()
        try {
            val inputStream = ClassLoader.getSystemResourceAsStream("application.properties")

            if (inputStream != null) {
                properties.load(inputStream)
                inputStream.close()
            } else {
                println("application.properties não encontrado no classpath. Tentando carregar do sistema de arquivos...")
                val fileInputStream = FileInputStream("application.properties")
                properties.load(fileInputStream)
                fileInputStream.close()
            }

            return properties.getProperty(pProperty)

        } catch (e: IOException) {
            System.err.println("Erro de I/O ao carregar application.properties: ${e.message}")
            return null
        } catch (e: Exception) {
            System.err.println("Ocorreu um erro inesperado ao carregar as propriedades: ${e.message}")
            return null
        }
    }
}