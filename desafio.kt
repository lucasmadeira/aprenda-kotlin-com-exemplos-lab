import kotlin.random.Random

// [Template no Kotlin Playground](https://pl.kotl.in/WcteahpyN)

//para facilitar o modelo, as perguntas só tem uma alternativa correta

enum class Nivel { BASICO, INTERMEDIARIO, DIFICIL }
enum class StatusCurso { EM_ANDAMENTO, FINALIZADO }

enum class StatusAprovacao { APROVADO, REPROVADO }

data class Usuario(val nome:String)

data class Alternativa(val alternativa: String, val correta: Boolean)

data class Pergunta(val pergunta: String, val alternativas: MutableList<Alternativa>){
    fun isValid():Boolean{
        return alternativas.filter { it.correta }.count() == 1
    }
}
class Quiz(val titulo:String, val porcentagemParaAprovar:Double){
    val perguntas = mutableListOf<Pergunta>()
    fun adicionarPergunta(pergunta: Pergunta) {
        perguntas.add(pergunta)
    }

    fun minimoParaAprovacao(): Int {
        return (perguntas.size * porcentagemParaAprovar).toInt() / 100

    }
}




data class Curso(val titulo: String, val descricao:String,var duracao: Int = 60,
                 val nivel: Nivel, val quiz: Quiz?, val xp:Int){
    override fun toString(): String {
        return "'$titulo')"
    }
}

data class RespostaPergunta(val resposta: String, val correta: Boolean)

class DesempenhoQuiz(val usuario: Usuario, val quiz:Quiz){

    val listPerguntasRespondidas = mutableListOf<RespostaPergunta>()

    var perguntaAtual = 0

    fun quizFinalizado() = quiz.perguntas.size == listPerguntasRespondidas.size

    fun aprovado() = listPerguntasRespondidas.filter { it.correta }.count() == quiz.minimoParaAprovacao()

    fun obterProximaPergunta() = quiz.perguntas.get(perguntaAtual)

    fun responderPergunta(resposta: RespostaPergunta) {
        listPerguntasRespondidas.add(resposta)
        perguntaAtual++
    }

    fun reiniciarQuiz(){
        perguntaAtual = 0
        listPerguntasRespondidas.clear()
    }


}

class AndamentoCurso(val usuario: Usuario, val curso: Curso, var statusCurso: StatusCurso = StatusCurso.EM_ANDAMENTO, val desempenhoQuiz: DesempenhoQuiz?){

    fun finalizarCurso() {
        statusCurso = StatusCurso.FINALIZADO
        desempenhoQuiz?.let {
            println("Curso ${curso.titulo}  FINALIZADO")
            statusCurso = if (it.quizFinalizado() && it.aprovado()) StatusCurso.FINALIZADO else StatusCurso.EM_ANDAMENTO
        }
    }
    fun obterXp() = if (statusCurso == StatusCurso.FINALIZADO)  curso.xp else 0;
}

data class ConteudoEducacional(val nome: String, val cursos: List<Curso>)

data class Formacao(val nome: String,val descricao:String, val conteudos: List<ConteudoEducacional>) {

    val inscritos = mutableListOf<Usuario>()

    val ranking = mutableListOf<Usuario>()

    fun matricular(usuario: Usuario) {
        this.inscritos.add(usuario)
        println("Usuario ${usuario.nome} matriculado com sucesso")
        println("Total de alunos matriculados: ${inscritos.size}")
    }
}

fun main() {

    val quizConhecendoKotlin = Quiz("Quiz curso conhecendo Kotlin", 80.0)
    val pergunta1 = Pergunta("Qual dos seguintes é um recurso exclusivo do Kotlin em comparação com o Java?",
        mutableListOf(Alternativa("Tipos de dados primitivos.", false),
            Alternativa("Anotações.", false),
            Alternativa("Extensões de funções.", true))
    )
    quizConhecendoKotlin.adicionarPergunta(pergunta1);

    val pergunta2 = Pergunta("Qual é o mecanismo usado em Kotlin para tratar exceções?",
        mutableListOf(Alternativa("try-catch.", true),
            Alternativa("when", false),
            Alternativa("for", false))
    )
    quizConhecendoKotlin.adicionarPergunta(pergunta2);
    val conhecendoKotlin =   Curso("Conhecendo Kotlin", "Aprenda a programar em Kotlin",2, Nivel.BASICO,quizConhecendoKotlin,20)

    val quizPoderFuncoesKotlin = Quiz("Quiz curso poder funcoes Kotlin", 80.0)
    val pergunta3 = Pergunta("Qual é a palavra-chave usada para definir uma função em Kotlin?",
        mutableListOf(Alternativa("method.", false),
            Alternativa("fun.", true),
            Alternativa("function.", false))
    )
    quizPoderFuncoesKotlin.adicionarPergunta(pergunta3);

    val pergunta4 = Pergunta("O que é uma função de extensão em Kotlin?",
        mutableListOf(Alternativa("Uma função que sempre estende o programa além de seu limite.", false),
            Alternativa("Uma função que é definida dentro de uma classe", false),
            Alternativa(" Uma função que é definida fora de uma classe e estende o comportamento de classes existentes", true))
    )
    quizPoderFuncoesKotlin.adicionarPergunta(pergunta4);

    val poderDasFuncoesKotlin =   Curso("Poder as funções Kotlin", "Aprenda o poder das funçoes no Kotlin",2, Nivel.BASICO,quizPoderFuncoesKotlin,20)
    val desmetificandoKotlin = ConteudoEducacional("Desmetificando Kotlin", listOf(conhecendoKotlin, poderDasFuncoesKotlin))



    // Crie um conteúdo educacional sobre Spring
    val conteudoSpring = ConteudoEducacional("Spring Framework", listOf(
        Curso("Introdução ao Spring", "Aprenda os conceitos básicos do Spring", 2, Nivel.BASICO, null, 20),
        Curso("Spring Boot Avançado", "Aprofunde-se no desenvolvimento com Spring Boot", 4, Nivel.INTERMEDIARIO, null, 40)
    ))

   val formacao = Formacao("Code Update TQI - Backend com Kotlin e Java",
    "Amplie suas aplicações back-end com códigos mais escaláveis e " +
            "performáticos para evoluir suas habilidades práticas e resolver desafios complexos " +
            "com foco nas tecnologias Kotlin e Java no novo programa em parceria com a TQI!", listOf(desmetificandoKotlin,conteudoSpring))


    // Matricule 4 alunos na formação
    val aluno1 = Usuario("Aluno 1")
    val aluno2 = Usuario("Aluno 2")


    formacao.matricular(aluno1)
    formacao.matricular(aluno2)

    val desempenhoAluno1Curso1 = DesempenhoQuiz(aluno1, formacao.conteudos[0].cursos[0].quiz!!)
    val desempenhoAluno1Curso2 = DesempenhoQuiz(aluno1, formacao.conteudos[0].cursos[1].quiz!!)
    val desempenhoAluno2Curso1 = DesempenhoQuiz(aluno2, formacao.conteudos[0].cursos[0].quiz!!)
    val desempenhoAluno2Curso2 = DesempenhoQuiz(aluno2, formacao.conteudos[0].cursos[1].quiz!!)

    val andamentosCursosAluno1 = mutableListOf(AndamentoCurso(aluno1, formacao.conteudos[0].cursos[0],StatusCurso.EM_ANDAMENTO,desempenhoAluno1Curso1),
            AndamentoCurso(aluno1, formacao.conteudos[0].cursos[1],StatusCurso.EM_ANDAMENTO,desempenhoAluno1Curso2),
            AndamentoCurso(aluno1, formacao.conteudos[1].cursos[0],StatusCurso.EM_ANDAMENTO,null),
            AndamentoCurso(aluno1, formacao.conteudos[1].cursos[1],StatusCurso.EM_ANDAMENTO,null))


    val andamentosCursosAluno2 = mutableListOf(AndamentoCurso(aluno2, formacao.conteudos[0].cursos[0],StatusCurso.EM_ANDAMENTO, desempenhoAluno2Curso1),
        AndamentoCurso(aluno2, formacao.conteudos[0].cursos[1],StatusCurso.EM_ANDAMENTO,desempenhoAluno2Curso2),
        AndamentoCurso(aluno2, formacao.conteudos[1].cursos[0],StatusCurso.EM_ANDAMENTO,null),
        AndamentoCurso(aluno2, formacao.conteudos[1].cursos[1],StatusCurso.EM_ANDAMENTO,null))



    var randomAlternativa :Int
    // Simule as respostas dos alunos aos quizzes
    while (!desempenhoAluno1Curso1.quizFinalizado()) {
        val pergunta = desempenhoAluno1Curso1.obterProximaPergunta()
        randomAlternativa = Random.nextInt(0,pergunta.alternativas.size-1)
        val resposta = RespostaPergunta(pergunta.alternativas[randomAlternativa].alternativa, pergunta.alternativas[randomAlternativa].correta)
        desempenhoAluno1Curso1.responderPergunta(resposta)
    }

    println("Aluno ${aluno1.nome} aprovado no curso ${formacao.conteudos[0].cursos[0]} ? ${desempenhoAluno1Curso1.aprovado()}")


    while (!desempenhoAluno1Curso2.quizFinalizado()) {
        val pergunta = desempenhoAluno1Curso2.obterProximaPergunta()
        randomAlternativa = Random.nextInt(0,pergunta.alternativas.size-1)
        val resposta = RespostaPergunta(pergunta.alternativas[randomAlternativa].alternativa, pergunta.alternativas[randomAlternativa].correta)
        desempenhoAluno1Curso2.responderPergunta(resposta)
    }

    println("Aluno ${aluno1.nome} aprovado no curso ${formacao.conteudos[0].cursos[1]} ? ${desempenhoAluno1Curso2.aprovado()}")


    while (!desempenhoAluno2Curso1.quizFinalizado()) {
        val pergunta = desempenhoAluno2Curso1.obterProximaPergunta()
        randomAlternativa = Random.nextInt(0,pergunta.alternativas.size-1)
        val resposta = RespostaPergunta(pergunta.alternativas[randomAlternativa].alternativa, pergunta.alternativas[randomAlternativa].correta)
        desempenhoAluno2Curso1.responderPergunta(resposta)
    }

    println("Aluno ${aluno2.nome} aprovado no curso ${formacao.conteudos[0].cursos[0]} ? ${desempenhoAluno2Curso1.aprovado()}")


    while (!desempenhoAluno2Curso2.quizFinalizado()) {
        val pergunta = desempenhoAluno2Curso2.obterProximaPergunta()
        randomAlternativa = Random.nextInt(0,pergunta.alternativas.size-1)
        val resposta = RespostaPergunta(pergunta.alternativas[randomAlternativa].alternativa, pergunta.alternativas[randomAlternativa].correta)
        desempenhoAluno2Curso2.responderPergunta(resposta)
    }

    println("Aluno ${aluno2.nome} aprovado no curso ${formacao.conteudos[0].cursos[1]} ? ${desempenhoAluno2Curso2.aprovado()}")


    //Finalizar cursos
    andamentosCursosAluno1.forEach(AndamentoCurso::finalizarCurso)
    andamentosCursosAluno2.forEach(AndamentoCurso::finalizarCurso)

    //imprimir os cursos finalizado
    println("Cursos finalizados pelo aluno 1")
    andamentosCursosAluno1.forEach { println(it.curso.titulo+" - "+it.statusCurso) }

    println("Cursos finalizados pelo aluno 2")
    andamentosCursosAluno2.forEach { println(it.curso.titulo+" - "+it.statusCurso) }


    //imprimir xp dos alunos
    println("Xp aluno ${aluno1.nome} = ${andamentosCursosAluno1.filter { it.statusCurso == StatusCurso.FINALIZADO }.sumOf { it.obterXp() }}")
    println("Xp aluno ${aluno2.nome} = ${andamentosCursosAluno2.filter { it.statusCurso == StatusCurso.FINALIZADO }.sumOf { it.obterXp() }}")

    TODO("Evoluir classe formção para ter ranking dos alunos")
    TODO("Evoluir classes usando DDD + Clean Architecture")
    TODO("Usar coroutines para simular alunos")
}
