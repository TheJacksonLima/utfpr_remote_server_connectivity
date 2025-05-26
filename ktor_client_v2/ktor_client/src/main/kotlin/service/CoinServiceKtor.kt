package org.example.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.model.Coin
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object CoinServiceKtor {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchAllCoins(): List<Coin> {
        val apiKey = System.getenv("API_KEY") ?: error("API_KEY não foi configurada nas variáveis de ambiente")
        val requestBuilder = HttpRequestBuilder().apply {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = getProperties("coin_gecko_base_url").toString()
                encodedPath = getProperties("coin_gecko_base_path").toString() + "list"
                parameters.append("vs_currency", "usd")
                parameters.append("x_cg_demo_api_key", apiKey)
            }
        }

        val coins: List<Coin> = client.request(requestBuilder).body()
        return coins;
    }

    suspend fun fetchCoinInfo(coinId : String): String {
        val apiKey = System.getenv("API_KEY") ?: error("API_KEY não foi configurada nas variáveis de ambiente")
        val requestBuilder = HttpRequestBuilder().apply {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = getProperties("coin_gecko_base_url").toString()
                encodedPath = getProperties("coin_gecko_base_path").toString() + coinId
                parameters.append("vs_currency", "usd")
                parameters.append("x_cg_demo_api_key", apiKey)
            }
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
        }
        return client.request(requestBuilder).bodyAsText()
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