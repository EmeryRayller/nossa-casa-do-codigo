package me.rayll.autor

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.transaction.Transactional

@MicronautTest(rollback = true)
internal class CadastroNovoAutorMockTest{

    @field:Inject
    lateinit var enderecoClient: EnderecoClient

    @field:Inject
    lateinit var autorRepository: AutorRepository

    @field:Inject @field:Client("/")
    lateinit var client: HttpClient

    @AfterEach
    internal fun tearDown() {
       autorRepository.deleteAll()
    }

    @Test
    fun `deve cadastrar um novo autor`() {

        val objectoResponse = object {
            val retorno = "{\"logradouro\":\"Rua Capetinga\",\"localidade\":\"Divinópolis\",\"uf\":\"Divinópolis\"}"
            val header = UriBuilder.of("/autor/{id}").expand(mutableMapOf("id" to "1"))
        }

        val novoAutorRequest =
            AutorRequest("Rayller", "rayller@email.com", "Autor de Ficção", "35501229", "510")

        Mockito.`when`(enderecoClient.consulta(novoAutorRequest.cep)).thenReturn(HttpResponse.ok(objectoResponse.retorno))

        val request = HttpRequest.POST("/autor", novoAutorRequest)

        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("location"))
        assertTrue(response.header("location").matches("/autor/\\d*".toRegex()))
    }

    @MockBean(EnderecoClient::class)
    fun enderecoMock(): EnderecoClient{
        return Mockito.mock(EnderecoClient::class.java)
    }
}