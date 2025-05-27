import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.example.service.QuoteServiceHttp
import org.example.service.QuoteServiceKtor
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

suspend fun main() = coroutineScope {
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
    val logFile = File("quotes_ktor_vs_http_$timestamp.txt")
    logFile.printWriter().use { writer ->

        fun log(msg: String) {
            writer.println(msg)
            println(msg)
        }

        var quotesK = QuoteServiceKtor.fetchAllQuotes()
        var quotesH = QuoteServiceHttp.fetchAllQuotes()

        log("\nComparando Ktor com HttpURLConnection em chamadas paralelas crescentes")

        for (reqCount in 1..300 step 10) {
            log("### Rodada com $reqCount requisições paralelas")

            val elapsedKtor = measureTimeMillis {
                coroutineScope {
                    val jobs = quotesK.take(reqCount).map { quote ->
                        async (Dispatchers.IO){
                            QuoteServiceKtor.fetchQuoteInfo(quote.id)
                        }
                    }
                    jobs.forEach { it.await() }
                }
            }

            val mediaKtor = elapsedKtor.toDouble() / reqCount
            log("Ktor - Total: ${elapsedKtor}ms | Média: %.4fms".format(mediaKtor))

            val elapsedHttp = measureTimeMillis {
                coroutineScope {
                    val jobs = quotesH.take(reqCount).map { quote ->
                        async (Dispatchers.IO){
                            QuoteServiceHttp.fetchQuoteInfo(quote.id)
                        }
                    }
                    jobs.forEach { it.await() }
                }
            }

            val mediaHttp = elapsedHttp.toDouble() / reqCount
            log("HTTP - Total: ${elapsedHttp}ms | Média: %.4fms".format(mediaHttp))

            log("_".repeat(60))
        }

    }
}
