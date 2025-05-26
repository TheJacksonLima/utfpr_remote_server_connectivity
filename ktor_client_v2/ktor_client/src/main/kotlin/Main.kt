import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.example.service.CoinServiceHttp
import org.example.service.CoinServiceKtor
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.LogManager
import kotlin.system.measureTimeMillis

suspend fun main() = coroutineScope {
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
    val logFile = File("ktor_vs_http_$timestamp.txt")
    logFile.printWriter().use { writer ->

        fun log(msg: String) {
            writer.println(msg)
            println(msg)
        }

        val TOTAL_REQ = 10

        log("Comparando Ktor com HttpURLConnection uma requisição por vez")

        var elapsedTime = measureTimeMillis {
            val coinsK = CoinServiceKtor.fetchAllCoins()
            val randomCoinsK = coinsK.take(TOTAL_REQ)

            randomCoinsK.forEach {
                val coinResponse = CoinServiceHttp.fetchCoinInfo(it.id)
            }
        }

        log("_".repeat(50))
        log("Ktor tempo total: ${elapsedTime}ms")
        log("Ktor tempo médio por requisição: ${elapsedTime / TOTAL_REQ}ms")
        log("_".repeat(50))

        elapsedTime = measureTimeMillis {
            val coinsH = CoinServiceHttp.fetchAllCoins()
            val randomCoinsH = coinsH.take(TOTAL_REQ)

            randomCoinsH.forEach {
                val coinResponse = CoinServiceHttp.fetchCoinInfo(it.id)
            }
        }

        log("HttpURLConnection tempo total: ${elapsedTime}ms")
        log("HttpURLConnection tempo médio por requisição: ${elapsedTime / TOTAL_REQ}ms")
        log("_".repeat(50))

        log("\nComparando Ktor com HttpURLConnection em chamadas paralelas crescentes")

        val coinsK = CoinServiceKtor.fetchAllCoins()
        val coinsH = CoinServiceHttp.fetchAllCoins()

        for (reqCount in 2.. 100 step 2) {
            log("### Rodada com $reqCount requisições paralelas")

            val elapsedKtor = measureTimeMillis {
                coroutineScope {
                    val jobs = coinsK.take(reqCount).map { coin ->
                        async {
                            CoinServiceKtor.fetchCoinInfo(coin.id)
                        }
                    }
                    jobs.forEach { it.await() }
                }
            }

            log("Ktor - Total: ${elapsedKtor}ms | Média: ${elapsedKtor / reqCount}ms")

            val elapsedHttp = measureTimeMillis {
                coroutineScope {
                    val jobs = coinsH.take(reqCount).map { coin ->
                        async {
                            CoinServiceHttp.fetchCoinInfo(coin.id)
                        }
                    }
                    jobs.forEach { it.await() }
                }
            }

            log("HTTP - Total: ${elapsedHttp}ms | Média: ${elapsedHttp / reqCount}ms")
            log("_".repeat(60))
        }
    }
}
