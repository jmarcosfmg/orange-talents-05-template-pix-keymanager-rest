package br.com.zup.orangetalents

import br.com.zup.orangetalents.commons.ChavePixGrpcServiceFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChaveControllerTests {

    @field:Inject
    lateinit var removeClienteStub: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var removeHttpClient: HttpClient


    @Test
    internal fun `deve registrar uma nova chave`() {
        val chave = RemoveChavePixRequest.newBuilder()
            .setClientId(UUID.randomUUID().toString())
            .setPixId(UUID.randomUUID().toString())
            .build()

        Mockito.`when`(removeClienteStub.remove(chave)).thenReturn(
            RemoveChavePixResponse.getDefaultInstance()
        )

        val response = removeHttpClient.toBlocking()
            .exchange(HttpRequest.DELETE<Any>("/api/v1/clientes/${chave.clientId}/pix/${chave.pixId}"), Any::class.java)
        assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = ChavePixGrpcServiceFactory::class)
    internal class MockitoRemoveStubFactory {

        @Singleton
        fun removeStubMock() = Mockito.mock(RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub::class.java)
    }
}