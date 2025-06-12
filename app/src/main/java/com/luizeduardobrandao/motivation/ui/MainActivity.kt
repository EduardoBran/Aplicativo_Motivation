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
import java.util.Locale

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

    // Guarda a frase exibida para restaurar após rotação
    private var currentPhrase: String = ""

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

        // --- Restaura estado em caso de rotação ---
        if (savedInstanceState != null) {

            // 1) Recupera o filtro salvo (ou ALL caso não exista)
            filter = savedInstanceState.getInt("filter", MotivationConstants.PHRASEFILTER.ALL)

            // 2) Recupera a frase salva (ou string vazia caso não exista)
            currentPhrase = savedInstanceState.getString("phrase") ?: ""

            // 3) Reseta a cor de todos os ícones para não selecionado (preto)
            binding.imageAllInclusive.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imageHappy.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.imageFunny.setColorFilter(ContextCompat.getColor(this, R.color.black))

            // 4) Destaca apenas o ícone correspondente ao filtro restaurado
            when (filter) {
                MotivationConstants.PHRASEFILTER.ALL   -> highlightFilter(binding.imageAllInclusive)
                MotivationConstants.PHRASEFILTER.HAPPY -> highlightFilter(binding.imageHappy)
                MotivationConstants.PHRASEFILTER.FUNNY -> highlightFilter(binding.imageFunny)
            }

            // 5) Reexibe exatamente a mesma frase que estava na tela
            binding.textviewPhrase.text = currentPhrase

        } else {
            // --- Estado inicial: sem filtro explícito (mas ALL visual) e frase nova ---
            handleFilter(R.id.image_all_inclusive)
            refreshPhrase()
        }

        // Registra os listeners de clique nos botões (delega para onClick(v: View)
        setListeners()

        // Exibe o nome do usuário na saudação
        showUserName()
    }

    // Salva o estado atual (filtro e frase) antes da Activity ser destruída em rotação.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("filter", filter)
        outState.putString("phrase", currentPhrase)
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

    // Vincula esta Activity como listener de clique para cada view de interesse.
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
    // Também armazena em currentPhrase para restauração futura.
    private fun refreshPhrase() {
        currentPhrase = phraseRepository.getPhrase(filter, Locale.getDefault().language)
        binding.textviewPhrase.text = currentPhrase
    }

    // Destaca o ícone passado como parâmetro, pintando-o de branco.
    // Usado por handleFilter() para mostrar qual é o filtro ativo.
    private fun highlightFilter(view: ImageView) {
        view.setColorFilter(ContextCompat.getColor(this, R.color.white))
    }

    // Atualiza o filtro interno e o destaque visual nos ícones.
    // Sempre pinta todos de preto antes de destacar o escolhido.
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
        // Ao trocar o filtro, já exibe a frase correspondente
        refreshPhrase()
    }
}

/*

Como as funções se conectam

- onCreate():
    1. enableEdgeToEdge() → permite layout imersivo atrás das barras de sistema
    2. inflate binding e setContentView() → carrega activity_main.xml
    3. setOnApplyWindowInsetsListener() → ajusta padding para status/navigation bars
    4. namePreferences = NamePreferences(this) → prepara leitura/escrita do nome do usuário
    5. if (savedInstanceState != null):
         • recupera 'filter' e 'currentPhrase' do Bundle
         • reseta cor dos ícones para preto
         • destaca ícone conforme 'filter'
         • reexibe 'currentPhrase'
       else:
         • handleFilter(R.id.image_all_inclusive) → define filtro ALL e destaca ícone
         • refreshPhrase() → busca e mostra uma frase nova
    6. setListeners() → configura cliques em ícones e botão 'Nova Frase'
    7. showUserName() → lê nome salvo e exibe saudação

- onSaveInstanceState(outState: Bundle)
    • chamado antes da Activity ser destruída (por rotação)
    • Bundle é uma estrutura chave-valor usada pelo Android para armazenar dados simples entre estados de Activity
    • grava 'filter' e 'currentPhrase' no Bundle para restauração futura

- onClick(v: View) delegado pelo setListeners() é chamado para qualquer clique registrado; delega a ação para:
    - handleFilter(id) quando ícones são clicados
    - refreshPhrase() quando o botão “Nova Frase” é clicado

- setListeners() registra this (a Activity) como ouvinte de clique em todas as Views de interesse.

- handleFilter(id) ajusta o estado interno filter e usa highlightFilter(view) para pintar o ícone ativo.

- refreshPhrase() busca do PhraseRepository uma frase de acordo com filter e atualiza o TextView e
                  também armazena em currentPhrase para restauração futura.

- showUserName() lê de SecurityPreferences o nome salvo e formata a saudação no TextView.

Essa arquitetura mantém cada responsabilidade isolada e o fluxo de dados claro:
usuário clica → onClick → lógica específica → atualização da UI.

 */