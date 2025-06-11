package com.luizeduardobrandao.motivation.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.motivation.R
import com.luizeduardobrandao.motivation.databinding.ActivityMainBinding
import com.luizeduardobrandao.motivation.helper.MotivationConstants
import com.luizeduardobrandao.motivation.repository.NamePreferences
import com.luizeduardobrandao.motivation.repository.PhraseRepository

// MainActivity agora implementa View.OnClickListener para receber todos os cliques via onClick()
class MainActivity : AppCompatActivity(), View.OnClickListener {

    // View Binding para acessar as Views de activity_main.xml sem findViewById
    private lateinit var binding: ActivityMainBinding

    // instancia para poder usar em vários métodos diferentes
    private lateinit var namePreferences: NamePreferences

    // Estado atual do filtro de frases (inicia com valor da constante ALL em MotivationConstants)
    private var filter: Int = MotivationConstants.PHRASEFILTER.ALL

    // Repositório que fornece frases a partir do filtro (nao precisa de lateinit pois nao recebe parâmetro)
    private val phraseRepository = PhraseRepository()

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

        // Cria a instância de SecurityPreferences para ler e escrever NamePreferences
        namePreferences = NamePreferences(this)

        // Registra os listeners de clique nos botões (delega para onClick(v: View)
        setListeners()

        // Inicializa o filtro padrão (ALL) e já destaca o ícone "ic_image_all_inclusive"
        handleFilter(R.id.image_all_inclusive)

        // Atualiza a frase mostrada usando o filtro atual
        refreshPhrase()

        // Exibe o nome do usuário na saudação
        showUserName()
    }

    // Recebe todos os cliques de Views registrados via "setOnClickListener(this)" (sempre apagar o "?" de "View?")
    // Identifica qual View foi clicada e chama a função apropriada.
    override fun onClick(v: View) {
        when (v.id){
            // Se um dos ícones de filtro for clicado, muda o filtro
            R.id.image_all_inclusive,
            R.id.image_happy,
            R.id.image_funny -> handleFilter(v.id)

            // Se o botão “Nova Frase” for clicado, atualiza a frase
            R.id.button_new_phrase -> refreshPhrase()
        }
    }

    // Configura o clique do botão “Nova Frase” para chamar nosso onClick
    // (Função para centralizar os elementos caso tenha mais do que um)
    private fun setListeners(){
        binding.buttonNewPhrase.setOnClickListener (this)
        binding.imageAllInclusive.setOnClickListener (this)
        binding.imageHappy.setOnClickListener (this)
        binding.imageFunny.setOnClickListener (this)
    }

    // Lê o nome do usuário de SharedPreferences e atualiza o TextView de saudação.
    private fun showUserName(){
        val name = namePreferences.getStoredString(MotivationConstants.KEY.PERSON_NAME)

        // binding.textviewName.text = name - exibe somente o nome
        binding.textviewName.text = getString(R.string.label_name_user, name) // Usa string formatada do resources: "Olá, %s!"

    }

    // Busca uma nova frase do repositório, passando o filtro atual e atualiza o texto na tela.
    private fun refreshPhrase() {
        binding.textviewPhrase.text = phraseRepository.getPhrase(filter)
    }

    // Destaca o ícone passado como parâmetro, pintando-o de branco.
    // Usado por handleFilter() para mostrar qual é o filtro ativo.
    private fun highlightFilter(view: ImageView) {
        view.setColorFilter(ContextCompat.getColor(this, R.color.white))
    }

    private fun handleFilter(id: Int) {

        // Primeiro, pinta todos os ícones de preto (estado não selecionado)
        binding.imageAllInclusive.setColorFilter(ContextCompat.getColor(this, R.color.black))
        binding.imageHappy.setColorFilter(ContextCompat.getColor(this, R.color.black))
        binding.imageFunny.setColorFilter(ContextCompat.getColor(this, R.color.black))

        // Depois, de acordo com o id, atualiza a variável filter e chama highlightFilter()
        when (id) {
            R.id.image_all_inclusive -> {
                filter = MotivationConstants.PHRASEFILTER.ALL
                highlightFilter(binding.imageAllInclusive)
            }
            R.id.image_happy -> {
                filter = MotivationConstants.PHRASEFILTER.HAPPY
                highlightFilter(binding.imageHappy)
            }
            R.id.image_funny -> {
                filter = MotivationConstants.PHRASEFILTER.FUNNY
                highlightFilter(binding.imageFunny)
            }
        }
    }
}

/*

Como as funções se conectam

- onCreate(): infla o layout, ajusta edge-to-edge, inicializa preferências, registra listeners e chama
              em sequência:

- 1. handleFilter(R.id.image_all) → destaca o ícone “Todos”
- 2. refreshPhrase() → mostra frase inicial
- 3. showUserName() → carrega e exibe o nome

- setListeners() registra this (a Activity) como ouvinte de clique em todas as Views de interesse.

- onClick(view) é chamado para qualquer clique registrado; delega a ação para:
    - handleFilter(id) quando ícones são clicados
    - refreshPhrase() quando o botão “Nova Frase” é clicado

- handleFilter(id) ajusta o estado interno filter e usa highlightFilter(view) para pintar o ícone ativo.

- refreshPhrase() busca do PhraseRepository uma frase de acordo com filter e atualiza o TextView.

- showUserName() lê de SecurityPreferences o nome salvo e formata a saudação no TextView.

Essa arquitetura mantém cada responsabilidade isolada e o fluxo de dados claro:
usuário clica → onClick → lógica específica → atualização da UI.

 */