package br.com.zup.orangetalents.dtos

import br.com.zup.orangetalents.ConsultaChavePixResponse
import com.google.type.TimeZone
import java.time.LocalDateTime
import java.time.ZoneOffset

class BuscaChaveControllerResponse(
    val clienteId:String,
    val pixId: String,
    val chave : ChaveInfo
){

    constructor(response : ConsultaChavePixResponse) : this(
        clienteId = response.clienteId,
        pixId = response.pixId,
        chave = ChaveInfo(
            tipoChave = response.chave.tipo.name,
            chave = response.chave.chave,
            criadaEm = LocalDateTime.ofEpochSecond(
                response.chave.criadaEm.seconds,
                response.chave.criadaEm.nanos,
                ZoneOffset.UTC
            ),
            conta = ContaInfo(
                tipoConta = response.chave.conta.tipo.name,
                instituicao = response.chave.conta.instituicao,
                nomeDoTitular = response.chave.conta.nomeDoTitular,
                cpfDoTitular = response.chave.conta.cpfDoTitular,
                agencia = response.chave.conta.agencia,
                numeroDaConta = response.chave.conta.numeroDaConta
            )
        )
    )

    class ChaveInfo(
        val tipoChave : String,
        val chave : String,
        val criadaEm : LocalDateTime,
        val conta : ContaInfo
    )

    class ContaInfo(
        val tipoConta : String,
        val instituicao : String,
        val nomeDoTitular : String,
        val cpfDoTitular : String,
        val agencia : String,
        val numeroDaConta : String
    )
}