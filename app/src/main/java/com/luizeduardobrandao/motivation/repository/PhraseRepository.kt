package com.luizeduardobrandao.motivation.repository

import com.luizeduardobrandao.motivation.helper.MotivationConstants
import kotlin.random.Random

data class Phrase(val description: String, val category: Int)

class PhraseRepository {

    private val happy = MotivationConstants.PHRASEFILTER.HAPPY
    private val sunny = MotivationConstants.PHRASEFILTER.SUNNY
    private val all = MotivationConstants.PHRASEFILTER.ALL

    private val listPhrases: List<Phrase> = listOf(
        Phrase("Não sabendo que era impossível, foi lá e fez.", happy),
        Phrase("Você não é derrotado quando perde, você é derrotado quando desiste!", happy),
        Phrase("Quando está mais escuro, vemos mais estrelas!", happy),
        Phrase("Insanidade é fazer sempre a mesma coisa e esperar um resultado diferente.", happy),
        Phrase("Não pare quando estiver cansado, pare quando tiver terminado.", happy),
        Phrase("O que você pode fazer agora que tem o maior impacto sobre o seu sucesso?", happy),
        Phrase("A melhor maneira de prever o futuro é inventá-lo.", sunny),
        Phrase("Você perde todas as chances que você não aproveita.", sunny),
        Phrase("Fracasso é o condimento que dá sabor ao sucesso.", sunny),
        Phrase(" Enquanto não estivermos comprometidos, haverá hesitação!", sunny),
        Phrase("Se você não sabe onde quer ir, qualquer caminho serve.", sunny),
        Phrase("Se você acredita, faz toda a diferença.", sunny),
        Phrase("Riscos devem ser corridos, porque o maior perigo é não arriscar nada!", sunny)
    )

    // Obtém frase aleatória de acordo com o filtro
    fun getPhrase(value: Int): String {
        // 1) Filtra a lista completa de frases:
        //    - mantém apenas frases cuja categoria seja igual ao 'value' selecionado
        //    - ou, se 'value' for o código ALL, mantém todas as frases
        val filteredPhrases = listPhrases.filter { (it.category == value || value == all) }

        // 2) Gera um índice aleatório entre 0 (inclusivo) e filteredPhrases.size (exclusivo)
        //    Usamos Random.nextInt(n) para obter um inteiro aleatório no intervalo [0, n-1]
        val randomIndex = Random.nextInt(filteredPhrases.size)

        // 3) Retorna a descrição da frase que está naquela posição aleatória
        //    filteredPhrases[randomIndex] é um objeto Phrase, e .description é a string que queremos
        return filteredPhrases[randomIndex].description
    }
}