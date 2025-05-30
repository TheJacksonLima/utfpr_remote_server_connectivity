import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    val writer = logFile.printWriter()
    val MAX_REQ = 100
    val INTERVAL_REQ = 1

    val log: (String) -> Unit = { msg ->
        writer.println(msg)
        println(msg)
    }

    try {

        log("Aquecer JIT e conexões...")
        warmUp(log)

        log("\nComparando Ktor com HttpURLConnection em chamadas paralelas crescentes")

        for (reqCount in 0..MAX_REQ step INTERVAL_REQ) {
            log("### Rodada com $reqCount requisições paralelas")

            if (reqCount % 2 == 0) {
                runHttpThenKtor(reqCount, log)
            } else {
                runKtorThenHttp(reqCount, log)
            }

            log("_".repeat(60))
        }
    } finally {
        writer.close()
    }
}

suspend fun runKtorThenHttp(reqCount: Int, log: (String) -> Unit) = coroutineScope {
    val elapsedKtor = measureTimeMillis {
        val jobs = buildRequestJobs(reqCount) { id ->
            QuoteServiceKtor.fetchQuoteInfo(id)
        }
        jobs.awaitAll()
    }
    log("Ktor - Total: ${elapsedKtor}ms | Média: %.4fms".format(elapsedKtor.toDouble() / reqCount))

    val elapsedHttp = measureTimeMillis {
        val jobs = buildRequestJobs(reqCount) { id ->
            QuoteServiceHttp.fetchQuoteInfo(id)
        }
        jobs.awaitAll()
    }
    log("HTTP - Total: ${elapsedHttp}ms | Média: %.4fms".format(elapsedHttp.toDouble() / reqCount))
}

suspend fun runHttpThenKtor(reqCount: Int, log: (String) -> Unit) = coroutineScope {
    val elapsedHttp = measureTimeMillis {
        val jobs = buildRequestJobs(reqCount) { id ->
            QuoteServiceHttp.fetchQuoteInfo(id)
        }
        jobs.awaitAll()
    }
    log("HTTP - Total: ${elapsedHttp}ms | Média: %.4fms".format(elapsedHttp.toDouble() / reqCount))

    val elapsedKtor = measureTimeMillis {
        val jobs = buildRequestJobs(reqCount) { id ->
            QuoteServiceKtor.fetchQuoteInfo(id)
        }
        jobs.awaitAll()
    }
    log("Ktor - Total: ${elapsedKtor}ms | Média: %.4fms".format(elapsedKtor.toDouble() / reqCount))
}

suspend fun <T> buildRequestJobs(reqCount: Int, block: suspend (Int) -> T) = coroutineScope {
    val ids = List(reqCount) { (it % 30) + 1 } // Sempre IDs válidos de 1 a 30
    ids.map { id ->
        async(Dispatchers.IO) {
            block(id)
        }
    }
}

suspend fun warmUp(log: (String) -> Unit) = coroutineScope {
    repeat(10) {
        QuoteServiceKtor.fetchQuoteInfo(1)
        QuoteServiceHttp.fetchQuoteInfo(1)
    }
    log("Warm-up completo.\n")
}
