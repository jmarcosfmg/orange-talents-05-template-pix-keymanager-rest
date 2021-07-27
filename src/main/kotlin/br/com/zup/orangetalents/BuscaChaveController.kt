package br.com.zup.orangetalents

import br.com.zup.orangetalents.commons.ValidUUID
import br.com.zup.orangetalents.dtos.BuscaChaveControllerResponse
import br.com.zup.orangetalents.dtos.ListaChaveControllerResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class BuscaChaveController(
    @Inject val keyManagerBuscaClient: ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub,
    @Inject val keyManagerListaClient: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub
) {
    @Get("/pix/{pixId}")
    fun buscaChave(
        @PathVariable clienteId: String,
        @PathVariable @ValidUUID pixId: String
    ): HttpResponse<Any> {
        val grpcResponse = keyManagerBuscaClient.consulta(
            ConsultaChavePixRequest.newBuilder().setPixId(
                ConsultaChavePixRequest.newBuilder()
                    .pixIdBuilder.setClienteId(clienteId).setPixId(pixId)
            ).build()
        )
        return HttpResponse.ok(BuscaChaveControllerResponse(grpcResponse))
    }

    @Get("/pix/all")
    fun listaChave(
        @PathVariable @ValidUUID clienteId: String
    ): HttpResponse<Any> {
        val grpcResponse = keyManagerListaClient.lista(
            ListaChavePixRequest.newBuilder().setClienteId(clienteId).build()
        )
        return HttpResponse.ok(ListaChaveControllerResponse(clienteId, grpcResponse))
    }
}

