package com.luizeduardobrandao.motivation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.motivation.R
import com.luizeduardobrandao.motivation.databinding.ActivityUserBinding
import com.luizeduardobrandao.motivation.helper.MotivationConstants
import com.luizeduardobrandao.motivation.repository.NamePreferences

class UserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserBinding
    private lateinit var namePreferences: NamePreferences // instancia para poder usar em vários métodos diferentes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa variáveis da classe
        namePreferences = NamePreferences(this)

        // Acesso aos elementos de interface
        binding.buttonSave.setOnClickListener(this)
    }

    // Lida com eventos de click
    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_save -> {
                handleButtonSave()
            }
        }
    }

    // Salva o nome do usuário para utilizações futuras
    private fun handleButtonSave() {

        // Obtém o nome
        val name = binding.edittextName.text.toString()

        // Verifica se usuário preencheu o nome
        if (name.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_mandatory_name), Toast.LENGTH_LONG).show()
        }
        else {
            // Salva os dados do usuário e redireciona para MainActivity
            // NamePreferences(this).storeString("NAME", name) SEM CONSTANTE ("NAME") e SEM INSTANCIA
            // namePreferences.storeString("NAME", name)       SEM CONSTANTE ("NAME")

            namePreferences.storeString(MotivationConstants.KEY.PERSON_NAME, name) // correto com instancia e constante

            // Navega para Main_Activity
            startActivity(Intent(this, MainActivity::class.java))

            // Impede que seja possível voltar a Activity
        }
    }

    private fun setListeners(){
        binding.buttonSave.setOnClickListener(this)
    }
}