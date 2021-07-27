package br.com.zup.orangetalents

import br.com.zup.orangetalents.dtos.InsereChavePixRestRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
open class InsereChaveController(
    @Inject val keyManagerClient: InsereChavePixServiceGrpc.InsereChavePixServiceBlockingStub
) {

    @Post("/pix")
    fun registraChave(@Body @Valid chave: InsereChavePixRestRequest): HttpResponse<Any> {
        val grpcResponse = keyManagerClient.insere(chave.toChavePixGrpcRequest())
        return HttpResponse.created(URI("/${chave.clientId}/pix/${grpcResponse.pixId.toString()}"))
    }
}

