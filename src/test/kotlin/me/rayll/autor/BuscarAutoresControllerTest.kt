package me.rayll.autor

import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class BuscarAutoresControllerTest{

    private lateinit var autor: Autor

    @field:Inject
    private lateinit var autorRepository: AutorRepository

    @field:Inject
    @field:Client("/")
    lateinit var cliente: HttpClient

    @BeforeEach
    internal fun setUp() {
        val enderecoResponse = EnderecoResponse("Rua Capetinga", "Divinópolis", "MG")
        val endereco = Endereco(enderecoResponse, "824", "35501231")
        autor = Autor("Rayller", "rayller@email.com", "Autor de ficção", endereco)
        autorRepository.save(autor)
    }

    @AfterEach
    internal fun tearDown() {
        autorRepository.deleteAll()
    }

    @Test
    internal fun `deve retornar detalhes de um autor`() {

        val response = cliente.toBlocking().exchange("/autor?email=${autor.email}", DetalhesAutorResponse::class.java)


        assertEquals(OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor.nome, response.body()!!.nome)
        assertEquals(autor.email, response.body()!!.email)
        assertEquals(autor.descricao, response.body()!!.descricao)
    }

}