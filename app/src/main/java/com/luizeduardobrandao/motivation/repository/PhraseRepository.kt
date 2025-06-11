package com.luizeduardobrandao.motivation.repository

import com.luizeduardobrandao.motivation.helper.MotivationConstants
import kotlin.random.Random

data class Phrase(val description: String, val category: Int)

class PhraseRepository {

    private val happy = MotivationConstants.PHRASEFILTER.HAPPY
    private val funny = MotivationConstants.PHRASEFILTER.FUNNY
    private val all = MotivationConstants.PHRASEFILTER.ALL

    private val listPhrases: List<Phrase> = listOf(
        Phrase("Não sabendo que era impossível, foi lá e fez.", happy),
        Phrase("Você não é derrotado quando perde, você é derrotado quando desiste!", happy),
        Phrase("Quando está mais escuro, vemos mais estrelas!", happy),
        Phrase("Insanidade é fazer sempre a mesma coisa e esperar um resultado diferente.", happy),
        Phrase("Não pare quando estiver cansado, pare quando tiver terminado.", happy),
        Phrase("O que você pode fazer agora que tem o maior impacto sobre o seu sucesso?", happy),
        Phrase("A melhor maneira de prever o futuro é inventá-lo.", happy),
        Phrase("Você perde todas as chances que você não aproveita.", happy),
        Phrase("Fracasso é o condimento que dá sabor ao sucesso.", happy),
        Phrase(" Enquanto não estivermos comprometidos, haverá hesitação!", happy),
        Phrase("Se você não sabe onde quer ir, qualquer caminho serve.", happy),
        Phrase("Se você acredita, faz toda a diferença.", happy),
        Phrase("Riscos devem ser corridos, porque o maior perigo é não arriscar nada!", happy),
        Phrase("Se a vida te der limões, aprenda a fazer uma caipirinha.", funny),
        Phrase("Corra atrás dos seus sonhos. Se não der, volte a dormir.", funny),
        Phrase("Acordei motivado... depois dormi de novo.", funny),
        Phrase("Você é único... igual a todo mundo.", funny),
        Phrase("O importante é não desistir... exceto da dieta, essa pode!", funny),
        Phrase("Você é capaz de tudo. Principalmente de fazer besteira com confiança.", funny),
        Phrase("Se tudo der errado hoje, relaxa: amanhã pode piorar.", funny),
        Phrase("Não tenha medo de fracassar. Tenha medo de não ter wi-fi!", funny),
        Phrase("Errar é humano. Culpar o colega é estratégia!", funny),
        Phrase("Grandes conquistas começam com um clique em 'adiar alarme'.", funny),
        Phrase("Acredite no seu potencial. Mesmo que ele esteja em modo soneca.", funny),
        Phrase("Acredite nos seus sonhos. Porque acordado tá difícil.", funny),
        Phrase("Não desista! Se até miojo vira refeição, você também tem chance.", funny)
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