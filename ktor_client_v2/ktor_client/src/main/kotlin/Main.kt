package org.example

import org.example.service.CoinService

suspend fun main() {
    val coins = CoinService.fetchCoins()
    if (coins != null) {
        println("Recebeu : ${coins.size} moedas")
    }
    else{
        println("Nenhuma moeda foi encontrada")
    }

}