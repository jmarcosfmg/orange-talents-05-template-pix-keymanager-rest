package br.com.zup.orangetalents.dtos

import br.com.zup.orangetalents.ListaChavePixResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

class ListaChaveControllerResponse(
    val clienteId: String,
    val chaves: List<Chave>
) {
    constructor(clienteId: String, response: ListaChavePixResponse) : this(
        clienteId = clienteId,
        chaves = response.chavesList.map {
            Chave(
                id = it.id,
                chave = it.chave,
                tipoChave = it.tipoChave.name,
                tipoConta = it.tipoConta.name,
                criadaEm = LocalDateTime.ofEpochSecond(
                    it.criadaEm.seconds,
                    it.criadaEm.nanos,
                    ZoneOffset.UTC
                ),
            )
        }.toList()
    )

    class Chave(
        val id: String,
        val chave: String,
        val tipoChave: String,
        val tipoConta: String,
        val criadaEm: LocalDateTime
    )
}