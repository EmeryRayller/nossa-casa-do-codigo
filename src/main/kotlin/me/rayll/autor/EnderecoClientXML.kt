package me.rayll.autor

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client


@Client("https://viacep.com.br/ws")
interface EnderecoClientXML {

    @Get("/{cep}/xml", consumes = [MediaType.APPLICATION_XML])
    fun consulta(@PathVariable cep: String): HttpResponse<String>
}
