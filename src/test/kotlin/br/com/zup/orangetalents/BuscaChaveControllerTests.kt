package br.com.zup.orangetalents

import br.com.zup.orangetalents.commons.ChavePixGrpcServiceFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class BuscaChaveControllerTests {

    @field:Inject
    lateinit var consultaClienteStub: ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub

    @field:Inject
    lateinit var listaClienteStub: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var buscaClient: HttpClient

    @Test
    internal fun `deve buscar uma unica chave`() {
        val clientId = UUID.randomUUID().toString();
        val pixId = UUID.randomUUID().toString();
        val chave = UUID.randomUUID().toString();

        val consultaChavePixRequest: ConsultaChavePixRequest =
            ConsultaChavePixRequest.newBuilder().setPixId(
                ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                    .setPixId(pixId).setClienteId(clientId)
                    .build()
            ).build()


        val consultaChavePixResponse: ConsultaChavePixResponse = ConsultaChavePixResponse.newBuilder()
            .setClienteId(clientId)
            .setPixId(pixId)
            .setChave(
                ConsultaChavePixResponse.ChavePix.newBuilder()
                    .setChave(chave)
                    .setTipo(TipoChaveGrpc.ALEATORIA)
                    .setCriadaEm(LocalDateTime.now().let {
                        val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                        Timestamp.newBuilder()
                            .setSeconds(createdAt.epochSecond)
                            .setNanos(createdAt.nano)
                            .build()
                    })
                    .setConta(
                        ConsultaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                            .setTipo(TipoContaGrpc.CORRENTE)
                            .setInstituicao("")
                            .setNomeDoTitular("Nome")
                            .setCpfDoTitular("12345678998")
                            .setAgencia("455445")
                            .setNumeroDaConta("455669")
                            .build()
                    )
            ).build()

        Mockito.`when`(
            consultaClienteStub.consulta(
                consultaChavePixRequest
            )
        ).thenReturn(
            consultaChavePixResponse
        )

        val response = buscaClient.toBlocking()
            .exchange(HttpRequest.GET<Any>("/api/v1/clientes/$clientId/pix/$pixId"), Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Test
    internal fun `deve listar chaves`() {
        val clientId = UUID.randomUUID().toString();

        val listaChavePixRequest : ListaChavePixRequest =
            ListaChavePixRequest.newBuilder().setClienteId(clientId).build()

        val listaChavePixResponse : ListaChavePixResponse =
            ListaChavePixResponse.newBuilder()
            .addAllChaves(
                arrayOf(
                        ListaChavePixResponse.Chave.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setClienteId(clientId)
                        .setChave(UUID.randomUUID().toString())
                        .setTipoChave(TipoChaveGrpc.ALEATORIA)
                        .setTipoConta(TipoContaGrpc.CORRENTE)
                        .setCriadaEm(LocalDateTime.now().let {
                            val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                            Timestamp.newBuilder()
                                .setSeconds(createdAt.epochSecond)
                                .setNanos(createdAt.nano)
                                .build()
                        })
                        .build(),
                    ListaChavePixResponse.Chave.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setClienteId(clientId)
                        .setChave(UUID.randomUUID().toString())
                        .setTipoChave(TipoChaveGrpc.ALEATORIA)
                        .setTipoConta(TipoContaGrpc.POUPANCA)
                        .setCriadaEm(LocalDateTime.now().let {
                            val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                            Timestamp.newBuilder()
                                .setSeconds(createdAt.epochSecond)
                                .setNanos(createdAt.nano)
                                .build()
                        })
                        .build()
                ).toList())
            .build()

        Mockito.`when`(
            listaClienteStub.lista(
                listaChavePixRequest
            )
        ).thenReturn(
            listaChavePixResponse
        )

        val response = buscaClient.toBlocking()
            .exchange(HttpRequest.GET<Any>("/api/v1/clientes/$clientId/pix/all"), Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Factory
    @Replaces(factory = ChavePixGrpcServiceFactory::class)
    internal class MockitoConsultaStubFactory {

        @Singleton
        fun consultaStubMock() =
            Mockito.mock(ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub::class.java)

        @Singleton
        fun listaStubMock() =
            Mockito.mock(ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub::class.java)
    }
}