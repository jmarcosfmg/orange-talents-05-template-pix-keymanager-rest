package br.com.zup.orangetalents

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject

@Validated
@Controller("/api/v1/clientes/{clienteId}")
open class RemoveChaveController(
    @Inject val keyManagerClient: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub
) {
    @Delete("/pix/{pixId}")
    fun removeChave(
        @PathVariable clienteId: UUID,
        @PathVariable pixId: UUID
    ): HttpResponse<Any> {
        keyManagerClient.remove(
            RemoveChavePixRequest.newBuilder()
                .setClientId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()
        )
        return HttpResponse.ok()
    }
}
