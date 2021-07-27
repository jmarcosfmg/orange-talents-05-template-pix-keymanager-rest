package br.com.zup.orangetalents

import br.com.zup.orangetalents.commons.ChavePixGrpcServiceFactory
import br.com.zup.orangetalents.dtos.InsereChavePixRestRequest
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class InsereChaveControllerTests {

    @field:Inject
    lateinit var registraClienteStub: InsereChavePixServiceGrpc.InsereChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpInsereClient: HttpClient


    @Test
    internal fun `deve registrar uma nova chave`() {
        val chave = InsereChavePixRestRequest(
            clientId = UUID.randomUUID().toString(),
            tipoChave = TipoChaveGrpc.CPF.toString(),
            tipoConta = TipoContaGrpc.POUPANCA.toString(),
            chave = "15548556570"
        )

        Mockito.`when`(registraClienteStub.insere(chave.toChavePixGrpcRequest())).thenReturn(
            ChavePixResponse.newBuilder().setPixId(chave.clientId).build()
        )

        val response = httpInsereClient.toBlocking().exchange(HttpRequest.POST("/api/v1/clientes/${chave.clientId}/pix", chave), InsereChavePixRestRequest::class.java)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(chave.clientId))
    }

    @Factory
    @Replaces(factory = ChavePixGrpcServiceFactory::class)
    internal class MockitoInsereStubFactory {

        @Singleton
        fun insereStubMock() = Mockito.mock(InsereChavePixServiceGrpc.InsereChavePixServiceBlockingStub::class.java)
    }
}