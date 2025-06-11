package com.luizeduardobrandao.motivation

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.motivation.databinding.ActivityMainBinding

// MainActivity agora implementa View.OnClickListener para receber callbacks de clique
class MainActivity : AppCompatActivity(), View.OnClickListener {

    // “binding” é o objeto gerado pelo View Binding que dá acesso a todas as Views de activity_main.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ativa a renderização edge-to-edge (layout atrás das barras de status e navegação)
        enableEdgeToEdge()

        // Infla o layout e inicializa o binding
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Define o conteúdo da Activity para a raiz do binding (RelativeLayout com id “main”)
        setContentView(binding.root)

        // Ajusta o padding para não ficar sob as barras de sistema (status/navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Registra os listeners de clique nos botões (delega para onClick(v: View)
        setListeners()
    }

    // Recebe todos os cliques de Views registrados via "setOnClickListener(this)" (sempre apagar o "?" de "View?"
    override fun onClick(v: View) {
        when (v.id){
            R.id.button_new_phrase -> {
                handleNewPhrase()
            }
        }
    }

    // Função para implementar/lidar com a lógica ao clicar no botão (handle = lidar)
    private fun handleNewPhrase(){
        // lógica ao clicar no botão
    }

    // Configura o clique do botão “Nova Frase” para chamar nosso onClick
    // ( Função para centralizar os elementos caso tenha mais do que um)
    private fun setListeners(){
        binding.buttonNewPhrase.setOnClickListener (this)
    }
}