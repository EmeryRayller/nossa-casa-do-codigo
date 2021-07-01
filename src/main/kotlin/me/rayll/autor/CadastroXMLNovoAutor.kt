package me.rayll.autor

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated

import javax.validation.Valid

@Validated
@Controller("/autor/xml")
class CadastroXMLNovoAutor(
    val autorRepository: AutorRepository,
    val enderecoClient: EnderecoClientXML){

    @Post
    fun cadastrarNovoAutor(@Body @Valid request: AutorRequest): HttpResponse<Any>{

        val httpResponse = enderecoClient.consulta(request.cep).body()

        var rua: String = ""
        var cidade: String = ""
        var estado: String = ""

        var build = XmlMapper.builder().build()

        if (!httpResponse.contains("erro")){
            rua = build.readValue(httpResponse, JsonNode::class.java).get("logradouro").asText()
            cidade = build.readValue(httpResponse, JsonNode::class.java).get("localidade").asText()
            estado = build.readValue(httpResponse, JsonNode::class.java).get("uf").asText()
        }

        val enderecoResponse = EnderecoResponse(rua, cidade, estado)

        var autorSalvo = request.paraAutor(enderecoResponse){ enderecoResponse, autorRequest ->
            val endereco = Endereco(enderecoResponse, request.numero, request.cep)
            Autor(autorRequest.nome, autorRequest.email, autorRequest.descricao, endereco)
        }

        autorRepository.save(autorSalvo)

        val url = UriBuilder.of("/autor/{id}").expand(mutableMapOf("id" to autorSalvo.id))
        return HttpResponse.created(url)
    }
}