package org.example.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.model.Coin
import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

object CoinService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchCoins(): List<Coin>? {
        println("Fetching all coins:")
        val apiKey = System.getenv("API_KEY") ?: error("API_KEY não foi configurada nas variáveis de ambiente")
        val url = getProperties(apiKey)
         client.get(it) {
                parameter("vs_currency", "usd")
                parameter("x_cg_demo_api_key", apiKey)
                accept(ContentType.Application.Json)
            }.body()
        }
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