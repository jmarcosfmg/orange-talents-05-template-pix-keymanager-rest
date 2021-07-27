package br.com.zup.orangetalents.commons

import br.com.zup.orangetalents.ConsultaChavePixServiceGrpc
import br.com.zup.orangetalents.InsereChavePixServiceGrpc
import br.com.zup.orangetalents.ListaChavePixServiceGrpc
import br.com.zup.orangetalents.RemoveChavePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class ChavePixGrpcServiceFactory (@GrpcChannel(value = "keyManager") val channel: ManagedChannel){

    @Singleton
    fun insereChave() = InsereChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun apagaChave() = RemoveChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun buscaChave() = ConsultaChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChave() = ListaChavePixServiceGrpc.newBlockingStub(channel)
}