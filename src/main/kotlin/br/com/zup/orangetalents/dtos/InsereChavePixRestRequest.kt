package br.com.zup.orangetalents.dtos

import br.com.zup.orangetalents.ChavePixRequest
import br.com.zup.orangetalents.TipoChaveGrpc
import br.com.zup.orangetalents.TipoContaGrpc
import br.com.zup.orangetalents.commons.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class InsereChavePixRestRequest(
    @field:NotBlank @ValidUUID val clientId: String,
    @field:NotBlank val tipoChave: String,
    @field:Size(max = 77) val chave: String?,
    @field:NotBlank val tipoConta: String
) {
    fun toChavePixGrpcRequest(): ChavePixRequest {
        return ChavePixRequest.newBuilder()
            .setCodigo(clientId)
            .setChave(chave ?: "")
            .setTipoConta(tipoConta.runCatching { TipoContaGrpc.valueOf(this) }
                .getOrDefault(TipoContaGrpc.TIPOCONTADESCONHECIDA))
            .setTipoChave(tipoChave.runCatching { TipoChaveGrpc.valueOf(this) }
                .getOrDefault(TipoChaveGrpc.TIPOCHAVEDESCONHECIDA))
            .build()
    }
}